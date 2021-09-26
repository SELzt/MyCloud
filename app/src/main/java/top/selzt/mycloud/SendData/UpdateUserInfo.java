package top.selzt.mycloud.SendData;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.RequestBody;
import okhttp3.Response;
import top.selzt.mycloud.ReceiveData.UserVo;
import top.selzt.mycloud.Util.Constance;
import top.selzt.mycloud.Util.SendRequest;
import top.selzt.mycloud.Util.UserMsg;

public class UpdateUserInfo {
    private Context mContext;
    public void go(String rolename, String mail, Context context){
        mContext = context;
        String url = "Http://" + Constance.SERVER_ADDR + "/api/updateInfo";
        NeedData data = new NeedData(rolename,mail);
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String msg = gson.toJson(data,NeedData.class);
        RequestBody requestBody = RequestBody.create(Constance.JSON,msg);
        SendRequest.Post(url,requestBody,callback);
    }
    private Callback callback = new Callback() {
        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {

        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            if (response.isSuccessful()){
                String body = response.body().string();
                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                UserVo userVo = gson.fromJson(body,UserVo.class);
                if(userVo.getCode().equals(Constance.SUCCESS)){
                    UserMsg userMsg = UserMsg.getInstance();
                    userMsg.setToken(userVo.getToken());
                    userMsg.setUserInfo(userVo.getUserInfo());
                    new GetUserInfo().go(mContext);
                    Log.e("GetUserInfo","更新用户信息成功");
                }
                else if(userVo.getCode().equals(Constance.FAIL)){
                    Log.e("GetUserInfo","更新用户信息失败");
                }
            }
        }
    };
    class NeedData{
        String username;
        String rolename;
        String mail;
        String token;
        public NeedData(String rolename,String mail){
            username = UserMsg.getInstance().getUserInfo().getUsername();
            token = UserMsg.getInstance().getToken();
            this.rolename = rolename;
            this.mail = mail;
        }
    }
}
