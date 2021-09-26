package top.selzt.mycloud.SendData;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.RequestBody;
import okhttp3.Response;
import top.selzt.mycloud.R;
import top.selzt.mycloud.ReceiveData.UserVo;
import top.selzt.mycloud.UserInfoActivity;
import top.selzt.mycloud.Util.Constance;
import top.selzt.mycloud.Util.SendRequest;
import top.selzt.mycloud.Util.UserMsg;
import top.selzt.mycloud.pojo.UserInfo;

public class GetUserInfo {
    private Context mContext;
    public void go(Context context){
        mContext = context;
        String url = "Http://" + Constance.SERVER_ADDR + "/api/getInfo";
        NeedData data = new NeedData();
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
            if(response.isSuccessful()){
                String body = response.body().string();
                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                UserVo userVo = gson.fromJson(body,UserVo.class);
                if(userVo.getCode().equals(Constance.SUCCESS)){
                    UserMsg userMsg = UserMsg.getInstance();
                    userMsg.setToken(userVo.getToken());
                    userMsg.setUserInfo(userVo.getUserInfo());
                    Intent intent = new Intent();
                    intent.setAction(Constance.RECEIVER_UPDATE_USERINFO);
                    mContext.sendBroadcast(intent);
                    ((UserInfoActivity)mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView tvRolename = ((UserInfoActivity)mContext).findViewById(R.id.infoRolename);
                            TextView tvMail = ((UserInfoActivity)mContext).findViewById(R.id.infoMail);
                            tvRolename.setText(userVo.getUserInfo().getRolename());
                            tvMail.setText(userVo.getUserInfo().getMail());
                        }
                    });
                    Log.e("GetUserInfo","获取用户信息成功");
                }
                else if(userVo.getCode().equals(Constance.FAIL)){
                    Log.e("GetUserInfo","获取用户信息失败");
                }
            }
        }
    };
    class NeedData{
        String username;
        String token;
        public NeedData(){
            UserInfo userInfo = UserMsg.getInstance().getUserInfo();
            username = userInfo.getUsername();
            token = UserMsg.getInstance().getToken();
        }
    }
}
