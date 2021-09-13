package top.selzt.mycloud.SendData;

import android.content.Context;
import android.os.Looper;
import android.widget.Switch;
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
import top.selzt.mycloud.ReceiveData.FileVo;
import top.selzt.mycloud.Util.Constance;
import top.selzt.mycloud.Util.SendRequest;
import top.selzt.mycloud.Util.UserMsg;

public class Rename {
    private Context mContext;
    public void go(Context context, String oldName, String newName){
        this.mContext = context;
        NeedData needData = new NeedData(oldName,newName);
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String msg = gson.toJson(needData,NeedData.class);
        RequestBody requestBody = RequestBody.create(Constance.JSON,msg);
        SendRequest.Post("http://"+Constance.SERVER_ADDR+"/api/rename",requestBody,callback);
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
                UserMsg.getInstance().setToken(fileVo.getToken());
                Looper.prepare();
                switch(fileVo.getCode()){
                    case Constance.RENAME_SUCCESS:
                        Toast.makeText(mContext,"重命名成功",Toast.LENGTH_SHORT).show();
                        break;
                    case Constance.RENAME_FAIL_EXISTS:
                        Toast.makeText(mContext,"文件名已存在",Toast.LENGTH_SHORT).show();
                        break;
                    case Constance.RENAME_FAIL_NOT_EXISTS:
                        Toast.makeText(mContext,"文件不存在",Toast.LENGTH_SHORT).show();
                        break;
                    case Constance.RENAME_FAIL:
                        Toast.makeText(mContext,"重命名失败",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(mContext,"重命名失败",Toast.LENGTH_SHORT).show();
                        break;
                }
                Looper.loop();
            }
        }
    };
    class NeedData{
        private String username;
        private String token;
        private String path;
        private String oldName;
        private String newName;
        public NeedData(){
            UserMsg userMsg = UserMsg.getInstance();
            username = userMsg.getUserInfo().getUsername();
            token = userMsg.getToken();
            path = userMsg.getNowPath();
        }
        public NeedData(String oldName,String newName){
            UserMsg userMsg = UserMsg.getInstance();
            username = userMsg.getUserInfo().getUsername();
            token = userMsg.getToken();
            path = userMsg.getNowPath();
            this.oldName = "/"+oldName;
            this.newName = "/"+newName;
        }

        public void setOldName(String oldName) {
            this.oldName = oldName;
        }

        public void setNewName(String newName) {
            this.newName = newName;
        }
    }
}
