package com.whg.eyepetizer.adapter

import android.graphics.Typeface
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.whg.eyepetizer.R
import com.whg.eyepetizer.utils.ImageLoadUtils
import com.whg.eyepetizer.utils.ctx
import kotlinx.android.synthetic.main.item_feed_result.view.*
import kotlinx.android.synthetic.main.item_veer.view.*
import com.whg.eyepetizer.mvp.model.bean.HotBean.ItemListBean.DataBean as FeedBean
import com.whg.eyepetizer.mvp.model.bean.KuulaListBean.PayloadBean.PostsBean as VeerBean


/**
 * Created by guanhuawei on 2017/8/29.
 */
class KuulaAdapter(val data: ArrayList<VeerBean>, val itemClick: (VeerBean) -> Unit) : RecyclerView.Adapter<KuulaAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.ctx).inflate(R.layout.item_veer, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(data[position])
    }


    class ViewHolder(view: View, val itemClick: (VeerBean) -> Unit) : RecyclerView.ViewHolder(view) {
        init {
            itemView.tv_title?.typeface = Typeface.createFromAsset(itemView.context?.assets, "fonts/FZLanTingHeiS-L-GB-Regular.TTF")
        }

        fun bindData(bean: VeerBean) {
            with(bean) {
                val context = itemView.context
                ViewCompat.setTransitionName(itemView.img, position.toString() + "_image")
                ImageLoadUtils.display(context, itemView.img, getKuulaCover(uuid))
                itemView.title.setText(user.name)
                itemView.description.setText(description)
                itemView.setOnClickListener { itemClick(this) }
            }
        }

        fun getKuulaCover(uuid: String): String {
            return "https://storage.kuula.co/$uuid/01-cover.jpg"
        }
    }


}