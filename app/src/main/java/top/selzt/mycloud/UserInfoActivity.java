package top.selzt.mycloud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.selzt.mycloud.SendData.UpdateUserInfo;
import top.selzt.mycloud.Util.Alert;
import top.selzt.mycloud.Util.Constance;
import top.selzt.mycloud.Util.UserMsg;
import top.selzt.mycloud.pojo.User;
import top.selzt.mycloud.pojo.UserInfo;

@Route(path = Constance.ROUTE_USER_INFO_URL)
public class UserInfoActivity extends AppCompatActivity {

    @BindView(R.id.infoUsername)
    TextView tvUsername;
    @BindView(R.id.infoRolename)
    TextView tvRolename;
    @BindView(R.id.infoMail)
    TextView tvMail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        UserInfo userInfo = UserMsg.getInstance().getUserInfo();
        tvUsername.setText(userInfo.getUsername());
        tvRolename.setText(userInfo.getRolename());
        tvMail.setText(userInfo.getMail());
    }
    @OnClick(R.id.infoBackBtn)
    public void onBackBtnClickListener(){
        finish();
    }
    @OnClick({R.id.ivInfoEditRolename,R.id.ivInfoEditMail})
    public void onEditUserMsgClickListener(View v){
        switch (v.getId()){
            case R.id.ivInfoEditRolename:
                final EditText editText01 = new EditText(UserInfoActivity.this);
                editText01.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18)});
                Alert.getInstance().needInputMsg(UserInfoActivity.this, "请输入昵称（20个字符内）",editText01, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String rolename = editText01.getText().toString();
                        if(rolename.replace(" ","").equals("")){
                            Alert.getInstance().SimpleMessage(UserInfoActivity.this,"昵称不能为空白字符串且不能为空");
                            return;
                        }
                        rolename = rolename.replace(" ","");
                        new UpdateUserInfo().go(rolename,null,UserInfoActivity.this);
                    }
                });
                break;
            case R.id.ivInfoEditMail:
                final EditText editText02 = new EditText(UserInfoActivity.this);
                editText02.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18)});
                Alert.getInstance().needInputMsg(UserInfoActivity.this, "请输入邮箱（20个字符内）",editText02, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail = editText02.getText().toString();
                        if(mail.replace(" ","").equals("")){
                            Alert.getInstance().SimpleMessage(UserInfoActivity.this,"邮箱不能为空白字符串且不能为空");
                            return;
                        }
                        mail = mail.replace(" ","");
                        Pattern pattern=Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}");
                        Matcher matcher=pattern.matcher(mail);
                        if(matcher.matches()){
                            new UpdateUserInfo().go(null,mail,UserInfoActivity.this);
                        }
                        else {
                            Alert.getInstance().SimpleMessage(UserInfoActivity.this,"邮箱格式不正确");
                        }

                    }
                });
                break;
            default:
                return;
        }
    }
}