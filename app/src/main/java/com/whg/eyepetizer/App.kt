package com.whg.eyepetizer

import android.content.Context
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import com.aitangba.swipeback.ActivityLifecycleHelper

/**
 * Created by guanhuawei on 2017/8/30.
 */
class App : MultiDexApplication(){

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(ActivityLifecycleHelper.build())
    }

    /**
     * 分包
     * @param base
     */
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

}