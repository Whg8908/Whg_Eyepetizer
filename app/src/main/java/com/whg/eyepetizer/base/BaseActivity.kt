package com.whg.eyepetizer.base

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.WindowManager
import com.aitangba.swipeback.SwipeBackHelper
import com.antonioleiva.weatherapp.ui.activities.ToolbarManager
import com.gyf.barlibrary.ImmersionBar


/**
 * Created by guanhuawei on 2017/8/30.
 */
abstract class BaseActivity : AppCompatActivity(), ToolbarManager, SwipeBackHelper.SlideBackManager {

    abstract val isFullScreen: Boolean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        doBeforeSetcontentView()
        setContentView(getLayoutId())
        this.initPresenter()
        this.initView()
    }


    abstract fun getLayoutId(): Int

    abstract fun initPresenter()

    abstract fun initView()


    private fun doBeforeSetcontentView() {
        if (!isFullScreen) {
            ImmersionBar.with(this).transparentBar().barAlpha(0.1f).fitsSystemWindows(true).init()
        }
    }

    fun setFullScreen() {
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    fun setNotFullScreen() {
        val attrs = window
                .attributes
        attrs.flags = attrs.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
        window.attributes = attrs
    }

    private var mSwipeBackHelper: SwipeBackHelper? = null

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (mSwipeBackHelper == null) {
            mSwipeBackHelper = SwipeBackHelper(this)
        }
        return mSwipeBackHelper!!.processTouchEvent(ev) || super.dispatchTouchEvent(ev)
    }

    override fun getSlideActivity(): Activity {
        return this
    }

    override fun supportSlideBack(): Boolean {
        return true
    }

    override fun canBeSlideBack(): Boolean {
        return true
    }

    override fun finish() {
        if (mSwipeBackHelper != null) {
            mSwipeBackHelper!!.finishSwipeImmediately()
            mSwipeBackHelper = null
        }
        super.finish()
    }
}