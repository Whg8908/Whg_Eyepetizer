package com.whg.eyepetizer.utils

import android.content.Context

/**
 * Created by guanhuawei on 2017/8/30.
 */
object UiUtils{
    fun dip2px(context: Context,dipValue:Float):Float{
        val scale :Float = context.resources.displayMetrics.density
        return dipValue * scale + 0.5f
    }
    fun px2dip(context: Context,pxValue : Float) :Int{
        val scale : Float = context.resources.displayMetrics.density
        return (pxValue/scale+0.5f).toInt()
    }
}