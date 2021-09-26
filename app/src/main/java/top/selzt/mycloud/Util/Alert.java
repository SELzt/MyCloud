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
import top.selzt.mycloud.SignUpActivity;

public class Alert {
    private static Alert alert;

    public synchronized static Alert getInstance(){
        if(alert == null)
            alert = new Alert();
        return alert;
    }
    public void needInputMsg(Context context, String title,EditText editText, DialogInterface.OnClickListener event){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setView(editText);
        builder.setNegativeButton("取消",null);
        builder.setPositiveButton("确定",event);
        builder.create().show();
    }
    public void CreateFolder(Context context){
        final EditText folderNameInput = new EditText(context);
        folderNameInput.setFilters(filters);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                    SimpleMessage(context,"文件名不能为空");
                    return;
                }
                new CreateFolder().go(context,"/"+folderName);
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
                    SimpleMessage(context,"文件名不能为空");
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
    public void SimpleMessage(Context context, String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示信息");
        builder.setMessage(msg);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(msg.equals("注册成功")){
                    ((SignUpActivity)context).finish();
                }
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

    public InputFilter[] getFilters() {
        return filters;
    }
}
