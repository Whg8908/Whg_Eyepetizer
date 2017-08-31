package com.whg.eyepetizer.utils

import android.content.Context
import com.whg.eyepetizer.mvp.model.bean.HotBean
import com.whg.eyepetizer.mvp.model.bean.VideoBean
import java.text.SimpleDateFormat

/**
 * Created by guanhuawei on 2017/8/30.
 */
object CVideoBeanUtils {
    fun setVideoBean(context: Context, dataBean: HotBean.ItemListBean.DataBean): VideoBean {
        var videoBean = VideoBean(dataBean.cover?.feed, dataBean.title, dataBean.description, dataBean.duration, dataBean.playUrl, dataBean.category, dataBean.cover?.blurred, dataBean.consumption?.collectionCount, dataBean.consumption?.shareCount, dataBean.consumption?.replyCount, System.currentTimeMillis())
        var url = SPUtils.getInstance(context, "beans").getString(dataBean.playUrl!!)
        if (url.equals("")) {
            var count = SPUtils.getInstance(context, "beans").getInt("count")
            if (count != -1) {
                count = count.inc()
            } else {
                count = 1
            }
            SPUtils.getInstance(context, "beans").put("count", count)
            SPUtils.getInstance(context, "beans").put("playUrl", dataBean.playUrl!!)
            ObjectSaveUtils.saveObject(context, "bean$count", videoBean)
        }
        return videoBean
    }

    fun triple(duration: Long, time: Long): Triple<String, String, String> {
        var minute = duration?.div(60)
        var second = duration?.minus((minute?.times(60)))
        var releaseTime = time
        var smf: SimpleDateFormat = SimpleDateFormat("MM-dd")
        var date = smf.format(releaseTime)
        var realMinute: String
        var realSecond: String
        if (minute!! < 10) {
            realMinute = "0" + minute
        } else {
            realMinute = minute.toString()
        }
        if (second!! < 10) {
            realSecond = "0" + second
        } else {
            realSecond = second.toString()
        }
        return Triple(date, realMinute, realSecond)
    }
}