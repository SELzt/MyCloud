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

public class Alert {
    private static Alert alert;

    public synchronized static Alert getInstance(){
        if(alert == null)
            alert = new Alert();
        return alert;
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
                if(folderName.equals("")){
                    Toast.makeText(homeActivity,"文件夹名不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                new CreateFolder().go(homeActivity,"/"+folderName);
            }
        });
        builder.create();
        builder.show();
    }
}
