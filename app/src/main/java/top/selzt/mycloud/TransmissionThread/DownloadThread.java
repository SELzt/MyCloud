package top.selzt.mycloud.TransmissionThread;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.RequestBody;
import okhttp3.Response;
import top.selzt.mycloud.Util.Constance;
import top.selzt.mycloud.Util.SendRequest;
import top.selzt.mycloud.Util.ThreadMap;
import top.selzt.mycloud.Util.UserMsg;

public class DownloadThread extends Thread{
    /**
     * 1.文件名
     * 2.文件大小
     * 3.下载量
     */
    private String filename;
    private double fileSize;
    private double downloadSize = 0;
    private RequestBody requestBody;
    private String url = "Http://" + Constance.SERVER_ADDR + "/api/download";
    private String url_getFileSize = "Http://" + Constance.SERVER_ADDR + "/api/getFileSize";
    public DownloadThread(String filename){
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public double getFileSize() {
        return fileSize;
    }

    public double getDownloadSize() {
        return downloadSize;
    }

    @Override
    public void run() {
        NeedData needData = new NeedData(filename);
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String msg = gson.toJson(needData,NeedData.class);
        requestBody = RequestBody.create(Constance.JSON,msg);
        ThreadMap.downloadThreadMap.put(filename,this);
        //先获取文件大小，获取成功才进行上传
        SendRequest.Post(url_getFileSize,requestBody,callback_size);
    }
    private Callback callback_size = new Callback() {
        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            e.printStackTrace();
        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            if(response.isSuccessful()){
                String body = response.body().string();
                long size = Long.valueOf(body);
                if(size ==0){
                    Log.e("getFileSize","获取文件大小异常");
                    return;
                }
                fileSize = size;
                SendRequest.Post(url,requestBody,callback);
            }
        }
    };
    private Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if(response.isSuccessful()){
                byte[] buffer = new byte[1024];
                InputStream input = response.body().byteStream();
                String base = Constance.LOCAL_PATH_BASE+"/"+UserMsg.getInstance().getUserInfo().getUsername() + "/";
                File bs = new File(base);
                if(!bs.exists()){
                    bs.mkdirs();
                }
                File f = new File( base+ filename);
                String prefix = filename.substring(0,filename.lastIndexOf("."));
                String suffix = filename.substring(filename.lastIndexOf("."));
                int i = 1;
                while(f.exists()){
                    f = new File(base+ prefix + "(" + i + ")" + suffix);
                    i++;
                }
                FileOutputStream output = new FileOutputStream(f);
                int len = 0;
                while((len = input.read(buffer))>0){
                    output.write(buffer,0,len);
                    downloadSize+=len; //更新下载进度
                    Log.i("download",downloadSize + "/" + fileSize);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("download","下载完成");
                input.close();
                output.close();
            }
        }
    };
    class NeedData{
        private String path;
        private String fileName;
        private String username;
        private String token;
        public NeedData(String filename){
            UserMsg userMsg = UserMsg.getInstance();
            path = userMsg.getNowPath();
            username = userMsg.getUserInfo().getUsername();
            token = userMsg.getToken();
            fileName = filename;
        }
    }
}
