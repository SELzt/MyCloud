package top.selzt.mycloud;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.selzt.mycloud.SendData.SignUp;
import top.selzt.mycloud.Util.Alert;
import top.selzt.mycloud.Util.Constance;

@Route(path = Constance.ROUTE_SIGN_UP_URL)
public class SignUpActivity extends AppCompatActivity {
    @BindView(R.id.etSignUsername)
    EditText etSignUsername;
    @BindView(R.id.etSignPassword)
    EditText etSignPassword;
    @BindView(R.id.etSignRolename)
    EditText etSignRolename;
    @BindView(R.id.etSignMail)
    EditText etSignMail;
    @BindView(R.id.signSubmit)
    TextView signSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        etSignUsername.setFilters(Alert.getInstance().getFilters());
        etSignPassword.setFilters(Alert.getInstance().getFilters());
    }
    @OnClick(R.id.signSubmit)
    void onSubmitClickListener(){
        String username = etSignUsername.getText().toString().replace(" ","");
        String password = etSignPassword.getText().toString().replace(" ","");
        String rolename = etSignRolename.getText().toString().replace(" ","");
        String mail = etSignMail.getText().toString().replace(" ","");
        if(username.equals("")){
            Alert.getInstance().SimpleMessage(this,"用户名不能为空");
            return;
        }
        if(password.equals("")){
            Alert.getInstance().SimpleMessage(this,"密码不能为空");
            return;
        }
        if(rolename.equals("")){
            Alert.getInstance().SimpleMessage(this,"昵称不能为空");
            return;
        }
        if(mail.equals("")){
            Alert.getInstance().SimpleMessage(this,"邮箱不能为空");
            return;
        }
        Pattern pattern=Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}");
        Matcher matcher=pattern.matcher(mail);
        if(matcher.matches()){
            if(password.length()<6){
                Alert.getInstance().SimpleMessage(this,"密码长度小于6位");
                return;
            }
            new SignUp().go(SignUpActivity.this,username,password,rolename,mail);
        }
        else {
            Alert.getInstance().SimpleMessage(this,"邮箱格式不正确");
            Log.i("SignUpMail","邮箱格式不正确");
        }
    }
    @OnClick(R.id.signBackBtn)
    void onSignBackBtnClickListener(){
        finish();
    }
}