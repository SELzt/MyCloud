package top.selzt.mycloud.TransmissionThread;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import top.selzt.mycloud.ReceiveData.FileVo;
import top.selzt.mycloud.Util.Constance;
import top.selzt.mycloud.Util.SendRequest;
import top.selzt.mycloud.Util.ThreadMap;
import top.selzt.mycloud.Util.UploadRequestBody;
import top.selzt.mycloud.Util.UserMsg;

public class UploadThread extends Thread{
    /**
     * 1.上传文件本地路径
     * 2.目标路径
     * 3.文件大小
     * 4.上传量
     * 5.文件名
     */
    //本地地址
    private String localPath;
    //目标地址
    private String destPath;
    //文件大小
    private long mFileSize;
    //上传量
    private long mUploadSize = 0;

    private File f;
    private String filename;
    public UploadThread(){}
    public UploadThread(String path){
        destPath = UserMsg.getInstance().getNowPath() + path.substring(path.lastIndexOf("/"));
        f = new File(path);
        filename = f.getName();
        ThreadMap.uploadThreadMap.put(filename,this);
    }

    public long getFileSize() {
        return mFileSize;
    }

    public long getUploadSize() {
        return mUploadSize;
    }

    public String getFilename() {
        return filename;
    }

    @Override
    public void run() {
        String url = "Http://" + Constance.SERVER_ADDR + "/api/upload";
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        UserMsg userMsg = UserMsg.getInstance();
        builder.addFormDataPart("file",f.getName(),RequestBody.create(Constance.FORM_DATA,f));
        builder.addFormDataPart(Constance.PATH,destPath);
        builder.addFormDataPart(Constance.USERNAME,userMsg.getUserInfo().getUsername());
        builder.addFormDataPart(Constance.TOKEN,userMsg.getToken());
        MultipartBody multipartBody = builder.build();
        UploadRequestBody uploadRequestBody = new UploadRequestBody(multipartBody, new UploadRequestBody.ProgressListener() {
            @Override
            public void getProgress(long uploadSize, long fileSize) {
                //处理上传进度
                mFileSize = fileSize;
                mUploadSize = uploadSize;
                Log.e("upload","fileSize:"+fileSize+" uploadSize" + uploadSize);
            }
        });
        SendRequest.Post(url,uploadRequestBody,callback);
    }
    private Callback callback = new Callback() {
        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {

        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            if(response.isSuccessful()){
                String body = response.body().string();
                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                Type jsonType = new TypeToken<FileVo>(){}.getType();
                FileVo fileVo = gson.fromJson(body,jsonType);
                if(fileVo.getCode() == Constance.UPLOAD_SUCCESS){
                    Log.e("upload","Success");
                }
                else{
                    Log.e("upload","Fail");
                }
            }
        }
    };

}
