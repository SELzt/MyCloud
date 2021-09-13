package top.selzt.mycloud.SendData;

import android.os.Looper;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.RequestBody;
import okhttp3.Response;
import top.selzt.mycloud.HomeActivity;
import top.selzt.mycloud.ReceiveData.FileVo;
import top.selzt.mycloud.Util.Constance;
import top.selzt.mycloud.Util.SendRequest;
import top.selzt.mycloud.Util.UserMsg;

public class CreateFolder {
    private HomeActivity homeActivity;
    public void go(HomeActivity homeActivity,String folderName){
        this.homeActivity = homeActivity;
        NeedData data = new NeedData(folderName);
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String msg = gson.toJson(data,NeedData.class);
        RequestBody requestBody = RequestBody.create(Constance.JSON,msg);
        SendRequest.Post("Http://"+Constance.SERVER_ADDR+"/api/createFolder",requestBody,callback);

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
                UserMsg userMsg = UserMsg.getInstance();
                userMsg.setToken(fileVo.getToken());
                Looper.prepare();

                if(fileVo.getCode()==Constance.CREATE_FOLDER_SUCCESS){
                    //正常情况，处理业务
                    Toast.makeText(homeActivity,"文件夹创建成功",Toast.LENGTH_SHORT).show();

                }
                else if(fileVo.getCode() == Constance.CREATE_FOLDER_EXISTS){
                    Toast.makeText(homeActivity,"文件夹已存在",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(homeActivity,"文件夹创建失败",Toast.LENGTH_SHORT).show();
                }
                Looper.loop();
            }
        }
    };
    class NeedData{
        private String username;
        private String token;
        private String path;
        private String folderName;
        public NeedData(){
            UserMsg userMsg = UserMsg.getInstance();
            username = userMsg.getUserInfo().getUsername();
            token = userMsg.getToken();
            path = userMsg.getNowPath();
        }

        public NeedData(String folderName){
            UserMsg userMsg = UserMsg.getInstance();
            username = userMsg.getUserInfo().getUsername();
            token = userMsg.getToken();
            path = userMsg.getNowPath();
            this.folderName = folderName;
        }

        public void setFolderName(String folderName) {
            this.folderName = folderName;
        }
    }
}
