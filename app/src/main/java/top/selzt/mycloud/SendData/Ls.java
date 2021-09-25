package top.selzt.mycloud.SendData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.RequestBody;
import okhttp3.Response;
import top.selzt.mycloud.Adapter.FileAdapter;
import top.selzt.mycloud.HomeActivity;
import top.selzt.mycloud.ReceiveData.FileVo;
import top.selzt.mycloud.Util.Constance;
import top.selzt.mycloud.Util.SendRequest;
import top.selzt.mycloud.Util.UserMsg;
import top.selzt.mycloud.pojo.FileDetail;

public class Ls {
    private String path;
    @Expose(serialize = false)
    private HomeActivity homeActivity;
    private List<FileDetail> fileList;
    private FileAdapter fileAdapter;
    public Ls(){
    }

    public void go(HomeActivity homeActivity, List<FileDetail> files, FileAdapter fileAdapter){
        this.homeActivity = homeActivity;
        this.fileList = files;
        this.fileAdapter = fileAdapter;
        String url = "http://" + Constance.SERVER_ADDR+"/api/ls";
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        //创建发送类
        Ls.SendMsg msg1 = new Ls.SendMsg(UserMsg.getInstance().getNowPath());
        String msg = gson.toJson(msg1,Ls.SendMsg.class);
        RequestBody requestBody = RequestBody.create(Constance.JSON,msg);
        SendRequest.Post(url,requestBody,callback);
    }
    private Callback callback = new Callback() {
        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            //请求失败
        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            if(response.isSuccessful()){
                //成功，处理数据
                String body = response.body().string();
                Gson gson = new Gson();
                Type jsonType = new TypeToken<FileVo>(){}.getType();
                //接收数据
                FileVo fileVo = gson.fromJson(body,jsonType);
                int code = fileVo.getCode();
                if(code == Constance.GET_LIST_SUCCESS){
                    UserMsg userMsg = UserMsg.getInstance();
                    userMsg.setToken(fileVo.getToken());
                    userMsg.setSuccess(true);
                    List<FileDetail> list = fileVo.getFilesList();
                    for(FileDetail f :list){
                        fileList.add(f);
                    }
                    homeActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fileAdapter.notifyDataSetChanged();
                        }
                    });
                }
                else {
                    //请求失败
                }

            }

        }
    };

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    class SendMsg{
        private String username;
        private String token;
        private String path;
        public SendMsg(){

        }
        public SendMsg(String path){
            UserMsg userMsg = UserMsg.getInstance();
            this.username = userMsg.getUserInfo().getUsername();
            this.token = userMsg.getToken();
            this.path = path;
        }
    }
}

