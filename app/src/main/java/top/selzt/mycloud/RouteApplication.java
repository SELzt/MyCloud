package top.selzt.mycloud;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.os.Bundle;

import com.alibaba.android.arouter.launcher.ARouter;

public class RouteApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if(BuildConfig.DEBUG){
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(this);
    }
}