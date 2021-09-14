package top.selzt.mycloud.SendData;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.RequestBody;
import okhttp3.Response;
import top.selzt.mycloud.Adapter.FileAdapter;
import top.selzt.mycloud.HomeActivity;
import top.selzt.mycloud.ReceiveData.FileVo;
import top.selzt.mycloud.Util.Constance;
import top.selzt.mycloud.Util.SendRequest;
import top.selzt.mycloud.Util.UserMsg;
import top.selzt.mycloud.pojo.FileDetail;

public class DeleteFile {
    private FileAdapter mFileAdapter;
    private List<FileDetail> mFiles;
    private Context mContext;
    private int position;
    public void go(Context mContext, FileAdapter mFileAdapter, String fileName, List<FileDetail> mFiles, int position){
        this.mContext = mContext;
        this.mFileAdapter = mFileAdapter;
        this.mFiles = mFiles;
        this.position = position;
        CreateFolder.NeedData data = new CreateFolder().new NeedData(fileName);
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String msg = gson.toJson(data,CreateFolder.NeedData.class);
        RequestBody requestBody = RequestBody.create(Constance.JSON,msg);
        SendRequest.Post("http://"+Constance.SERVER_ADDR+"/api/delete",requestBody,callback);
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
                Type jsonType = new TypeToken<FileVo>(){}.getType();
                FileVo fileVo = gson.fromJson(body,jsonType);
                UserMsg.getInstance().setToken(fileVo.getToken());
                ((HomeActivity)mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(fileVo.getCode() == Constance.DELETE_FILE_SUCCESS){
                            mFileAdapter.notifyItemRemoved(position);
                            mFiles.remove(position);
                            mFileAdapter.notifyItemRangeChanged(position,mFileAdapter.getItemCount());
                            Toast.makeText(mContext,"删除成功",Toast.LENGTH_SHORT).show();
                        }
                        else if(fileVo.getCode() == Constance.DELETE_FILE_FAIL) {
                            Toast.makeText(mContext,"删除失败",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(mContext,"文件不存在，请刷新后重试",Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        }
    };
}
