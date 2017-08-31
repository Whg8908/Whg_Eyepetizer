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
import kotlinx.android.synthetic.main.item_home.view.*
import com.whg.eyepetizer.mvp.model.bean.HomeBean.IssueListBean.ItemListBean as HomeBean


/**
 * Created by guanhuawei on 2017/8/29.
 */
class HomeAdapter(val data: ArrayList<HomeBean>, val itemClick: (HomeBean.DataBean) -> Unit) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.ctx).inflate(R.layout.item_home, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(data[position])
    }


    class ViewHolder(view: View, val itemClick: (HomeBean.DataBean) -> Unit) : RecyclerView.ViewHolder(view) {
        init {
            itemView?.tv_title?.typeface = Typeface.createFromAsset(itemView.context?.assets, "fonts/FZLanTingHeiS-DB1-GB-Regular.TTF")
        }

        fun bindData(bean: HomeBean) {
            with(bean.data!!) {
                val context = itemView.context
                var (date, realMinute: String, realSecond: String) = CVideoBeanUtils.triple(duration!!,releaseTime!!)

                ImageLoadUtils.display(context,itemView?.iv_photo, cover?.feed as String)
                itemView?.tv_title?.text = title
                itemView?.tv_detail?.text = "发布于 $category / $realMinute:$realSecond"
                if(author!=null){
                    ImageLoadUtils.display(context,itemView?.iv_user,author?.icon as String)
                }else{
                    itemView?.iv_user?.visibility = View.GONE
                }

                itemView.setOnClickListener { itemClick(this) }
            }
        }
    }
}