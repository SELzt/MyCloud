package top.selzt.mycloud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import top.selzt.mycloud.SendData.Login;
import top.selzt.mycloud.pojo.User;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.TvLoginText)
    TextView tvLogin;
    @BindView(R.id.showPwd)
    ImageView showPWD;

    @BindView(R.id.EtLoginPassword)
    EditText etLoginPassword;
    @BindView(R.id.EtLoginUsername)
    EditText etLoginUsername;

    @BindView(R.id.cvLoginBackground)
    ImageView cvLoginBackground;
    @BindView(R.id.cvLogin)
    CardView cvLogin;
    Boolean showPWDFlag;
    Boolean canClickOne = false;
    Boolean canClickTwo = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        init();

    }
    private void init(){
        ButterKnife.bind(this);
        tvLogin.getPaint().setFakeBoldText(true);
        showPWDFlag = false;
        etLoginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        showPWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!showPWDFlag){
                    //显示密码
                    showPWD.setImageResource(R.drawable.ic_baseline_visibility_24);
                    etLoginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else {
                    //隐藏密码
                    showPWD.setImageResource(R.drawable.ic_baseline_visibility_24);
                    etLoginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                showPWDFlag = !showPWDFlag;
            }
        });
        cvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                String username = etLoginUsername.getText().toString().replace(" ","");
                String password = etLoginPassword.getText().toString().replace(" ","");
                if(username.equals(""))
                    return;
                if(password.equals(""))
                    return;
                user.setUsername(username);
                user.setPassword(password);
                new Login().go(user,MainActivity.this);
            }
        });
        if(!etLoginUsername.getText().toString().equals("")){
            canClickOne = true;
        }
        if(!etLoginPassword.getText().toString().equals("")){
            canClickTwo = true;
        }
        if(canClickOne&&canClickTwo){
            cvLoginBackground.setBackgroundColor(Color.parseColor("#3366CC"));
        }
    }

    @OnTextChanged(R.id.EtLoginUsername)
    public void onTextChangeOne(CharSequence text){
        if(!text.toString().equals(""))
            canClickOne = true;
        else
            canClickOne = false;
        if(canClickOne&&canClickTwo)
            cvLoginBackground.setBackgroundColor(Color.parseColor("#3366CC"));
        else
            cvLoginBackground.setBackgroundColor(Color.parseColor("#BFBFBF"));
    }
    @OnTextChanged(R.id.EtLoginPassword)
    public void onTextChangeTwo(CharSequence text){
        if(!text.toString().equals(""))
            canClickTwo = true;
        else
            canClickTwo = false;
        if(canClickOne&&canClickTwo)
            cvLoginBackground.setBackgroundColor(Color.parseColor("#3366CC"));
        else
            cvLoginBackground.setBackgroundColor(Color.parseColor("#BFBFBF"));
    }

}