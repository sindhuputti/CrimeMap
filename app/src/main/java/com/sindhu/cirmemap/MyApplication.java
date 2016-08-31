package com.sindhu.cirmemap;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.quickblox.core.QBSettings;

public class MyApplication extends Application {

    private static final String APP_ID = "40257";
    private static final String AUTH_KEY = "3NDKFEnUeEVm6JW";
    private static final String AUTH_SECRET = "R7s-T4mVx8dK7b9";
    private static final String ACCOUNT_KEY = "VYsExJ61CKSLk9EpxjYS";
    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initApplication();
    }

    private void initApplication() {
        instance = this;

        // Set application credentials
        //
        QBSettings.getInstance().init(getApplicationContext(), String.valueOf(APP_ID), AUTH_KEY,
                AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    public int getAppVersion() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
}