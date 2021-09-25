package top.selzt.mycloud.SendData;

import android.util.Log;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
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
import top.selzt.mycloud.MainActivity;
import top.selzt.mycloud.Util.Alert;
import top.selzt.mycloud.Util.Constance;
import top.selzt.mycloud.Util.SendRequest;
import top.selzt.mycloud.Util.UserMsg;
import top.selzt.mycloud.pojo.User;
import top.selzt.mycloud.ReceiveData.UserVo;

public class Login {
    private final String url = "http://"+ Constance.SERVER_ADDR+"/api/login";
    private MainActivity mainActivity;
    public void go(User user,MainActivity mainActivity){
        this.mainActivity = mainActivity;
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.disableHtmlEscaping().create();
        String msg = gson.toJson(user,User.class);
        RequestBody requestBody = RequestBody.create(Constance.JSON,msg);
        SendRequest.Post(url,requestBody,callback);
    }
    private Callback callback = new Callback() {
        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            Log.e("Login","登陆错误");
            e.printStackTrace();
        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            mainActivity.runOnUiThread(new Runnable() {
                String body = response.body().string();
                public void run() {
                    if(response.isSuccessful()){
                        Gson gson = new Gson();
                        Type jsonType = new TypeToken<UserVo>(){}.getType();
                        UserVo userVo = gson.fromJson(body,jsonType);
                        String code = userVo.getCode();
                        if(code.equals("20000")){
                            UserMsg userMsg = UserMsg.getInstance();
                            userMsg.setToken(userVo.getToken());
                            userMsg.setUserInfo(userVo.getUserInfo());
                            Toast.makeText(mainActivity,"登陆成功",Toast.LENGTH_SHORT).show();
                            ARouter.getInstance().build(Constance.ROUTE_HOMEACTIVITY_URL).navigation();
                            mainActivity.finish();
                        }
                        else if (code.equals("20001")){
                            Alert.getInstance().SimpleMessage(mainActivity,"密码错误");
                        }
                        else if (code.equals("20002")){
                            Alert.getInstance().SimpleMessage(mainActivity,"帐号不存在");
                        }
                        else {

                        }
                    }
                }
            });
        }
    };
}
