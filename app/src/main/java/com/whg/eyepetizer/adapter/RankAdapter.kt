package com.whg.eyepetizer.adapter

import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.whg.eyepetizer.R
import com.whg.eyepetizer.utils.CVideoBeanUtils
import com.whg.eyepetizer.utils.ImageLoadUtils
import com.whg.eyepetizer.utils.ctx
import kotlinx.android.synthetic.main.item_rank.view.*
import com.whg.eyepetizer.mvp.model.bean.HotBean.ItemListBean.DataBean as RankBean

/**
 * Created by guanhuawei on 2017/8/29.
 */
class RankAdapter(val data: ArrayList<RankBean>, val itemClick: (RankBean) -> Unit)
    : RecyclerView.Adapter<RankAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.ctx).inflate(R.layout.item_rank, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(data[position])
    }


    class ViewHolder(view: View, val itemClick: (RankBean) -> Unit) : RecyclerView.ViewHolder(view) {
        val context = itemView.context
        fun bindData(bean: RankBean) {
            with(bean) {
                itemView.tv_title?.typeface = Typeface.createFromAsset(context?.assets, "fonts/FZLanTingHeiS-L-GB-Regular.TTF")

                ImageLoadUtils.display(context!!, itemView.iv_photo, cover?.feed!!)
                itemView.tv_title?.text = title
                var (date, realMinute: String, realSecond: String) = CVideoBeanUtils.triple(duration,releaseTime)

                itemView?.tv_time?.text = "$category / $realMinute'$realSecond''"
                itemView.setOnClickListener { itemClick(this) }
            }
        }
    }
}