package com.antonioleiva.weatherapp.ui.activities

import android.support.v7.graphics.drawable.DrawerArrowDrawable
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import com.whg.eyepetizer.utils.ctx
import com.whg.eyepetizer.utils.slideEnter
import com.whg.eyepetizer.utils.slideExit

/**
 * Created by guanhuawei on 2017/8/30.
 */
interface ToolbarManager {

    val toolbar: Toolbar

    var toolbarTitle: String
        get() = toolbar?.title.toString()
        set(value) {
            toolbar?.title = value
        }


    fun enableHomeAsUp(up: () -> Unit) {
        toolbar?.navigationIcon = createUpDrawable()
        toolbar?.setNavigationOnClickListener { up() }
    }

    private fun createUpDrawable() = DrawerArrowDrawable(toolbar?.ctx).apply { progress = 1f }

    fun attachToScroll(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (dy > 0) toolbar?.slideExit() else toolbar?.slideEnter()
            }
        })
    }
}