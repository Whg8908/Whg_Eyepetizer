package com.whg.eyepetizer.ui

import android.support.v7.widget.Toolbar
import com.whg.eyepetizer.R
import com.whg.eyepetizer.base.BaseActivity
import org.jetbrains.anko.find

/**
 * Created by guanhuawei on 2017/8/30.
 */
class AdviseActivity: BaseActivity(){
    override val isFullScreen: Boolean = false

    override val toolbar by lazy { find<Toolbar>(R.id.toolbar) }


    override fun getLayoutId(): Int {
        return R.layout.activity_advise
    }

    override fun initPresenter() {

    }

    override fun initView() {
        settoolbar()
    }

    fun settoolbar(){
        toolbarTitle = "意见反馈"
        enableHomeAsUp { onBackPressed() }
    }
}