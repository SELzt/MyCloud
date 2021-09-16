package top.selzt.mycloud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ListView;

import com.alibaba.android.arouter.facade.annotation.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.selzt.mycloud.Adapter.UploadAdapter;
import top.selzt.mycloud.TransmissionThread.UploadThread;
import top.selzt.mycloud.Util.Constance;
import top.selzt.mycloud.Util.ThreadMap;

@Route(path = Constance.ROUTE_TRANSMISSION_URL)
public class TransmissionActivity extends AppCompatActivity {

    @BindView(R.id.transmissionRecycle)
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.popup_transmission_panel);
        ButterKnife.bind(this);
        List<UploadThread> threadList = new ArrayList<>();
        for (Map.Entry<String, UploadThread> entry : ThreadMap.uploadThreadMap.entrySet()) {
            threadList.add(entry.getValue());
        }
        UploadAdapter uploadAdapter = new UploadAdapter(TransmissionActivity.this, R.layout.popup_transmission_item, threadList);
        LinearLayoutManager llm = new LinearLayoutManager(this);//线性布局
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(uploadAdapter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
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
        }).start();
    }
}