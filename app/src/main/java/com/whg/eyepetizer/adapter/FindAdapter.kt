package com.whg.eyepetizer.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.whg.eyepetizer.R
import com.whg.eyepetizer.mvp.model.bean.FindBean
import com.whg.eyepetizer.utils.ImageLoadUtils
import com.whg.eyepetizer.utils.UiUtils
import com.whg.eyepetizer.utils.ctx
import kotlinx.android.synthetic.main.item_find.view.*

/**
 * Created by guanhuawei on 2017/8/29.
 */
class FindAdapter(val data: ArrayList<FindBean>, val itemClick: (FindBean) -> Unit)
    : RecyclerView.Adapter<FindAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.ctx).inflate(R.layout.item_find, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(data[position])
        holder.itemView.setPadding(0, 0, if (position % 2 == 0) UiUtils.dip2px(holder.itemView.context, 3.0f).toInt() else 0, 0)
    }

    class ViewHolder(view: View, val itemClick: (FindBean) -> Unit) : RecyclerView.ViewHolder(view) {
        fun bindData(bean: FindBean) {
            with(bean) {
                ImageLoadUtils.display(itemView.context!!, itemView.iv_photo, bgPicture!!)
                itemView.tv_title?.text = name
                itemView.setOnClickListener { itemClick(this) }
            }
        }
    }
}