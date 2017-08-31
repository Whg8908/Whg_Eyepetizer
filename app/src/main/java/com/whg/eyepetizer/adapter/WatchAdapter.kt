package com.whg.eyepetizer.adapter

import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.whg.eyepetizer.R
import com.whg.eyepetizer.mvp.model.bean.VideoBean
import com.whg.eyepetizer.utils.CVideoBeanUtils
import com.whg.eyepetizer.utils.ImageLoadUtils
import com.whg.eyepetizer.utils.ctx
import kotlinx.android.synthetic.main.item_feed_result.view.*
import com.whg.eyepetizer.mvp.model.bean.HotBean.ItemListBean.DataBean as FeedBean


/**
 * Created by guanhuawei on 2017/8/29.
 */
class WatchAdapter(val data: ArrayList<VideoBean>, val itemClick: (VideoBean) -> Unit) : RecyclerView.Adapter<WatchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.ctx).inflate(R.layout.item_feed_result, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(data[position])
    }


    class ViewHolder(view: View, val itemClick: (VideoBean) -> Unit) : RecyclerView.ViewHolder(view) {
        init {
            itemView.tv_title?.typeface = Typeface.createFromAsset(itemView.context?.assets, "fonts/FZLanTingHeiS-L-GB-Regular.TTF")
        }

        fun bindData(bean: VideoBean) {
            with(bean) {
                ImageLoadUtils.display(itemView.context!!, itemView?.iv_photo, feed!!)
                itemView.tv_title?.text = title
                var (date, realMinute: String, realSecond: String) = CVideoBeanUtils.triple(duration!!,time)
                itemView.tv_detail.text = "$category / $realMinute'$realSecond'' / $date"
                itemView.setOnClickListener { itemClick(this) }
            }
        }
    }
}