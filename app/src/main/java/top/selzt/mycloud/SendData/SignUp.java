package top.selzt.mycloud.SendData;

import android.content.Context;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.RequestBody;
import okhttp3.Response;
import top.selzt.mycloud.SignUpActivity;
import top.selzt.mycloud.Util.Alert;
import top.selzt.mycloud.Util.Constance;
import top.selzt.mycloud.Util.SendRequest;

public class SignUp {
    Context mContext;
    public void go(Context context,String username,String password,String rolename,String mail){
        mContext = context;
        String url = "Http://" + Constance.SERVER_ADDR + "/api/sign";
        NeedData data = new NeedData(username,password,rolename,mail);
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
                int code = Integer.valueOf(body);
                Looper.prepare();
                switch (code){
                    case 20000:
                        Alert.getInstance().SimpleMessage(mContext,"注册成功");

                        break;
                    case 20001:
                        Alert.getInstance().SimpleMessage(mContext,"用户名为空");
                        break;
                    case 20002:
                        Alert.getInstance().SimpleMessage(mContext,"密码为空");
                        break;
                    case 20003:
                        Alert.getInstance().SimpleMessage(mContext,"昵称为空");
                        break;
                    case 20004:
                        Alert.getInstance().SimpleMessage(mContext,"邮箱为空");
                        break;
                    case 20005:
                        Alert.getInstance().SimpleMessage(mContext,"输入信息不合法");
                        break;
                    case 20006:
                        Alert.getInstance().SimpleMessage(mContext,"注册失败");
                        break;
                    case 30001:
                        Alert.getInstance().SimpleMessage(mContext,"用户名已存在");
                        break;
                }
                Looper.loop();
            }
        }
    };
    class NeedData{
        String username;
        String password;
        String rolename;
        String mail;
        public NeedData(){}
        public NeedData(String username,String password,String rolename,String mail){
            this.username = username;
            this.password = password;
            this.rolename = rolename;
            this.mail = mail;
        }
    }
}
