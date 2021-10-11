package top.selzt.mycloud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.selzt.mycloud.Adapter.FileAdapter;
import top.selzt.mycloud.SendData.Ls;
import top.selzt.mycloud.TransmissionThread.UploadThread;
import top.selzt.mycloud.Util.Alert;
import top.selzt.mycloud.Util.Constance;
import top.selzt.mycloud.Util.UserMsg;
import top.selzt.mycloud.pojo.FileDetail;

@Route(path = Constance.ROUTE_HOMEACTIVITY_URL)
public class HomeActivity extends AppCompatActivity {
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private final static int REQUEST_PERMISSIONS_CODE = 1;
    private String path;
    //  回调码
    private final static int FILE_RESULT_CODE = 400;
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
    @BindView(R.id.tvHomeRolename)
    TextView tvHomeRolename;
    List<FileDetail> files;
    FileAdapter fileAdapter;
    Ls ls;//获取文件列表
    UpdateUserInfoReceiver infoReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_home);
        init();
    }
    private void init(){
        ButterKnife.bind(this);
        tvHomeRolename.setText(UserMsg.getInstance().getUserInfo().getRolename());
        files = new ArrayList<>();
        ls = new Ls();
        fileAdapter = new FileAdapter(this,R.layout.home_recycleview_item,files);
        fileAdapter.setOnItemClickListener(new FileAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(RecyclerView parent, View view, int position, FileDetail file) {
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
                            //超时设置为2000ms
                            int count = 0;
                            while(!UserMsg.getInstance().isSuccess()&&count<20){
                                count++;
                                Thread.sleep(100);
                            }
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
        getOrder();
        //注册用户信息更新广播
        infoReceiver = new UpdateUserInfoReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constance.RECEIVER_UPDATE_USERINFO);
        registerReceiver(infoReceiver,intentFilter);
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
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("*/*");//无类型限制
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        startActivityForResult(intent, FILE_RESULT_CODE);
                        return true;
                    case R.id.transmissionManage:
                        //传输管理，管理正在传输或已经传输完成的文件
                        ARouter.getInstance().build(Constance.ROUTE_TRANSMISSION_URL).navigation();
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
        //返回按钮
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
    @OnClick(R.id.rlHomeLeftUser)
    public void onLeftUserClickListener(){
        ARouter.getInstance().build(Constance.ROUTE_USER_INFO_URL).navigation();
    }
    @OnClick(R.id.rlHomeLeftPanel)
    public void onLeftPanelClickListener(){

    }
    //获取权限
    private void getOrder(){
                boolean firstLaunch = false;
                String shareFile = getResources().getString(R.string.shareFilename);
                SharedPreferences spFile = getSharedPreferences(shareFile, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = spFile.edit();
                if (spFile.getBoolean("First", true)) {
                    firstLaunch = true;
                    editor.putBoolean("First", false);
                    editor.apply();
                }
                if(ContextCompat.checkSelfPermission(HomeActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){

                }
                else if(firstLaunch||ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(PERMISSIONS_STORAGE,REQUEST_PERMISSIONS_CODE);
                    }
                }
            }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case REQUEST_PERMISSIONS_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }
                else {
                    Toast.makeText(this, "权限申请失败", Toast.LENGTH_SHORT).show();

                }
                break;
            default:

        }
    }
    //用户信息更新广播
    public class UpdateUserInfoReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            tvHomeRolename.setText(UserMsg.getInstance().getUserInfo().getRolename());
        }
    }
    //退出登陆
    @OnClick(R.id.tv_home_exit)
    public void ExitListener(){
        System.exit(0);
    }
    //获取文件真实路径
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        path = null;
        if(resultCode == Activity.RESULT_OK){
            Uri uri = data.getData();
            if ("file".equalsIgnoreCase(uri.getScheme())){//使用第三方应用打开
                path = uri.getPath();
                return;
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                path = getPath(this, uri);
                if(path == null){
                    Toast.makeText(HomeActivity.this,"该文件暂时无法获取",Toast.LENGTH_SHORT).show();
                    return;
                }
                new UploadThread(path).start();

            } else {//4.4以下下系统调用方法
                path = getRealPathFromURI(uri);

            }
        }
    }

    //测试
    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(null!=cursor&&cursor.moveToFirst()){;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }
        return res;
    }

    /**
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     */
    @SuppressLint("NewApi")
    public String getPath(final Context context, final Uri uri) {
        try {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
                else {
                    return Environment.getExternalStorageDirectory().getPath() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),Long.valueOf(id) );
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(infoReceiver);
        super.onDestroy();
    }
}