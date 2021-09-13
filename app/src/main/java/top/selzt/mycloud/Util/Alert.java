package top.selzt.mycloud.Util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;
import android.widget.Toast;

import top.selzt.mycloud.HomeActivity;
import top.selzt.mycloud.SendData.CreateFolder;
import top.selzt.mycloud.SendData.Rename;

public class Alert {
    private static Alert alert;

    public synchronized static Alert getInstance(){
        if(alert == null)
            alert = new Alert();
        return alert;
    }
    public void CreateFolder(HomeActivity homeActivity){
        final EditText folderNameInput = new EditText(homeActivity);
        folderNameInput.setFilters(filters);
        AlertDialog.Builder builder = new AlertDialog.Builder(homeActivity);
        builder.setTitle("请输入新文件夹名");
        builder.setView(folderNameInput);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //doNothing
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //doSomeThing
                String folderName = folderNameInput.getText().toString();
                if(folderName.replace(" ","").equals("")){
                    Toast.makeText(homeActivity,"文件夹名不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                new CreateFolder().go(homeActivity,"/"+folderName);
            }
        });
        builder.create();
        builder.show();
    }
    public void Rename(Context context,String oldName){
        final EditText newNameInput = new EditText(context);
        newNameInput.setFilters(filters);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("请输入新文件名");
        builder.setView(newNameInput);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newName = newNameInput.getText().toString();
                if(newName.replace(" ","").equals("")){
                    //空字符串
                    Toast.makeText(context,"文件名不能为空",Toast.LENGTH_SHORT);
                    return;
                }
                new Rename().go(context,oldName,newName);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create();
        builder.show();
    }
    private InputFilter[] filters = new InputFilter[]{
            new InputFilter() {
                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                    if(end + dstart>10)
                        return "";
                    int mStart = start;
                    int mEnd = end;
                    while(mStart<mEnd){
                        char c = source.charAt(mStart);
                        if((c>='a'&&c<='z')||(c>='A'&&c<='Z')||(c>='0'&&c<='9')){
                            mStart++;
                            continue;
                        }
                        source = source.subSequence(0,mStart);
                        return source;
                    }
                    return source;
                }
            }
    };
}
