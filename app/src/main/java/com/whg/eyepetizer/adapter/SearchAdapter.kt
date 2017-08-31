package com.whg.eyepetizer.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.flexbox.FlexboxLayoutManager
import com.whg.eyepetizer.R
import com.whg.eyepetizer.utils.ctx
import kotlinx.android.synthetic.main.item_find.view.*

/**
 * Created by guanhuawei on 2017/8/29.
 */
class SearchAdapter(val data: ArrayList<String>, val itemClick: (String) -> Unit)
    : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.ctx).inflate(R.layout.item_search, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(data[position])
    }


    class ViewHolder(view: View, val itemClick: (String) -> Unit) : RecyclerView.ViewHolder(view) {
        fun bindData(string: String) {
            with(string) {
                itemView.tv_title.text = string
                val params = itemView?.tv_title?.layoutParams
                if (params is FlexboxLayoutManager.LayoutParams) {
                    (itemView?.tv_title?.layoutParams as FlexboxLayoutManager.LayoutParams).flexGrow = 1.0f
                }
                itemView.setOnClickListener { itemClick(this) }
            }
        }
    }
}