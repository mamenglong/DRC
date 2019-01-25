package com.mml.drc.application;

import android.app.Application;

import com.mml.drc.activities.LoginActivity;
import com.mml.drc.activities.MainActivity;

import org.litepal.LitePal;
/**
 * 项目名称：
 * @author Long
 * @date 2018/6/25
 * 修改时间：2018/6/25 8:42
 */
public class MyApplication extends Application {
    private static MyApplication application;
    private LoginActivity loginActivity;
    private MainActivity mainActivity;
    public static MyApplication getApplication() {
        return application;
    }

    public LoginActivity getLoginActivity() {
        return loginActivity;
    }

    public void setLoginActivity(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if(application==null) {
            application=this;
        }
        LitePal.initialize(this);
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

}
