package top.selzt.mycloud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.selzt.mycloud.Adapter.FileAdapter;
import top.selzt.mycloud.SendData.Ls;
import top.selzt.mycloud.Util.Alert;
import top.selzt.mycloud.Util.Constance;
import top.selzt.mycloud.Util.UserMsg;
import top.selzt.mycloud.pojo.File;

@Route(path = Constance.ROUTE_HOMEACTIVITY_URL)
public class HomeActivity extends AppCompatActivity {
    @BindView(R.id.homeRecycleView)
    RecyclerView recyclerView;
    @BindView(R.id.ivPopMenu)
    ImageView ivPopMenu;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.ivMenuBack)
    ImageView ivMenuBack;//返回按钮
    @BindView(R.id.ivMenuUser)
    ImageView ivMenuUser;//菜单栏用户头像
    @BindView(R.id.cvMenuUser)
    CardView cvMenuUser;//菜单栏用户头像父元素
    List<File> files;
    FileAdapter fileAdapter;
    Ls ls;//获取文件列表
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_home);
        new Thread(new Runnable() {
            @Override
            public void run() {
                init();
            }
        }).start();
    }
    private void init(){
        ButterKnife.bind(this);
        files = new ArrayList<>();
        ls = new Ls();
        fileAdapter = new FileAdapter(this,R.layout.home_recycleview_item,files);
        fileAdapter.setOnItemClickListener(new FileAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(RecyclerView parent, View view, int position,File file) {
                if(file.getFileType().equals("1")){
                    //文件夹，可以进入 添加路径到UserMsg
                    UserMsg userMsg = UserMsg.getInstance();
                    userMsg.setNowPath(userMsg.getNowPath()+"/"+file.getFileName());
                    ivMenuBack.setVisibility(View.VISIBLE);
                    cvMenuUser.setVisibility(View.INVISIBLE);
                    files.clear();
                    ls.go(HomeActivity.this,files,fileAdapter);

                }
                else {
                    //文件，暂不进行处理
                }
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(this);//线性布局
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(fileAdapter);
        ls.go(this,files,fileAdapter);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                UserMsg userMsg = UserMsg.getInstance();
                userMsg.setSuccess(false);
                files.clear();
                ls.go(HomeActivity.this,files,fileAdapter);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //超时设置为200ms
                            Thread.sleep(200);
                            if(UserMsg.getInstance().isSuccess())
                                refreshLayout.finishRefresh(true);
                            else
                                refreshLayout.finishRefresh(false);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();;
            }
        });
    }

    @OnClick({R.id.ivPopMenu})
    public void menuClickListener(){
        PopupMenu popupMenu = new PopupMenu(this,ivPopMenu);
        popupMenu.inflate(R.menu.menu_menubar);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.createFolder:
                        //创建新文件夹
                        Alert.getInstance().CreateFolder(HomeActivity.this);
                        return true;
                    case R.id.upload:
                        //上传文件
                        return true;
                    case R.id.downloadManage:
                        //下载管理，管理本地已下载好的文件
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }
    @OnClick(R.id.ivMenuBack)
    public void menuBackClickListener(){
        UserMsg userMsg = UserMsg.getInstance();
        String path = userMsg.getNowPath();
        if(path.equals("")){
            return;
        }
        path = path.substring(0,path.lastIndexOf("/"));
        if(path.equals("")){
            ivMenuBack.setVisibility(View.INVISIBLE);
            cvMenuUser.setVisibility(View.VISIBLE);
        }
        userMsg.setNowPath(path);
        files.clear();
        ls.go(HomeActivity.this,files,fileAdapter);
    }
}