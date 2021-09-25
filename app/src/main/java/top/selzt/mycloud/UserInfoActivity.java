package top.selzt.mycloud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
                Alert.getInstance().needInputMsg(UserInfoActivity.this, "请输入昵称（20个字符内）", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                break;
            case R.id.ivInfoEditMail:
                Alert.getInstance().needInputMsg(UserInfoActivity.this, "请输入邮箱（20个字符内）", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                break;
            default:
                return;
        }
    }
}