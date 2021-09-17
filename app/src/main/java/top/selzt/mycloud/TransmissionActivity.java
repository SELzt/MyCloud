package top.selzt.mycloud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.selzt.mycloud.Adapter.UploadAdapter;
import top.selzt.mycloud.TransmissionThread.UploadThread;
import top.selzt.mycloud.Util.Constance;
import top.selzt.mycloud.Util.ThreadMap;

@Route(path = Constance.ROUTE_TRANSMISSION_URL)
public class TransmissionActivity extends AppCompatActivity {

    @BindView(R.id.transmissionRecycle)
    RecyclerView recyclerView;
    @BindView(R.id.transmissionStateChangeLayout)
    RelativeLayout stateLayout;
    @BindView(R.id.tvPopupState)
    TextView tvPopupState;

    UploadThreadNotify notifyUploadThread;
    Thread notifyDownloadThread;
    List<UploadThread> threadList;
    UploadAdapter uploadAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.transmission_panel);
        ButterKnife.bind(this);
        threadList = new ArrayList<>();
        for (Map.Entry<String, UploadThread> entry : ThreadMap.uploadThreadMap.entrySet()) {
            threadList.add(entry.getValue());
        }
        uploadAdapter = new UploadAdapter(TransmissionActivity.this, R.layout.transmission_item, threadList);
        LinearLayoutManager llm = new LinearLayoutManager(this);//线性布局
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(uploadAdapter);
        notifyUploadThread =  new UploadThreadNotify();
        notifyUploadThread.start();
    }
    @OnClick(R.id.transmissionStateChangeLayout)
    public void stateLayoutClickListener(){
        PopupMenu popupMenu = new PopupMenu(this,stateLayout);
        popupMenu.inflate(R.menu.menu_transmission_state);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.transmissionUploading:
                        //正在上传
                        tvPopupState.setText("正在上传");
                        if(notifyUploadThread.isAlive()){
                            notifyUploadThread.flag = false;
                        }
                        /*if(notifyDownloadThread.isAlive()){

                        }*/
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                threadList.clear();
                                for (Map.Entry<String, UploadThread> entry : ThreadMap.uploadThreadMap.entrySet()) {
                                    threadList.add(entry.getValue());
                                }
                                notifyUploadThread = new UploadThreadNotify();
                                notifyUploadThread.start();
                            }
                        }).start();
                        return true;
                    case R.id.transmissionDownloading:
                        //正在下载
                        if(notifyUploadThread.isAlive()){
                            notifyUploadThread.flag = false;
                        }
                        /*if(notifyDownloadThread.isAlive()){
                            notifyDownloadThread.stop();
                        }*/
                        tvPopupState.setText("正在下载");

                        return true;
                    case R.id.transmissionDownloaded:
                        //已下载
                        tvPopupState.setText("已下载");
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }
    class UploadThreadNotify extends Thread{
        boolean flag = true;
        @Override
        public void run() {
            while (flag){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        uploadAdapter.notifyDataSetChanged();
                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}