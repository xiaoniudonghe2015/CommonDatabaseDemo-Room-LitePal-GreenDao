package com.example.commondatabasedemo;

import android.app.Application;

import com.example.commondatabasedemo.room.AppExecutors;

import org.litepal.LitePal;

public class DemoApplication extends Application {
    private AppExecutors mAppExecutors;
    static DemoApplication demoApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        demoApplication = this;
        mAppExecutors = new AppExecutors();

        //litepal
        LitePal.initialize(this);
    }

    public AppExecutors getAppExecutors() {
        return mAppExecutors;
    }

    public static DemoApplication getApplication() {
        return demoApplication;
    }
}
