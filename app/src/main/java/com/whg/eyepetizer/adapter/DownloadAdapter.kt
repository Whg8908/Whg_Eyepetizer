package com.whg.eyepetizer.adapter

import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.whg.eyepetizer.R
import com.whg.eyepetizer.mvp.model.bean.VideoBean
import com.whg.eyepetizer.utils.ImageLoadUtils
import com.whg.eyepetizer.utils.SPUtils
import com.whg.eyepetizer.utils.ctx
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.item_download.view.*
import zlc.season.rxdownload2.RxDownload
import zlc.season.rxdownload2.entity.DownloadFlag

/**
 * Created by guanhuawei on 2017/8/30.
 */
class DownloadAdapter(val data: ArrayList<VideoBean>, val itemClick: (VideoBean) -> Unit, val itemLongClick: (Int) -> Unit) : RecyclerView.Adapter<DownloadAdapter.ViewHolder>() {
    lateinit var viewHolder: ViewHolder

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.ctx).inflate(R.layout.item_download, parent, false)
        viewHolder = ViewHolder(view, itemClick, itemLongClick)
        return viewHolder
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(data[position], position)
    }

    class ViewHolder(view: View, val itemClick: (VideoBean) -> Unit, val itemLongClick: (Int) -> Unit) : RecyclerView.ViewHolder(view) {
        var isDownload = false
        var hasLoaded = false
        lateinit var disposable: Disposable

        init {
            itemView.tv_title?.typeface = Typeface.createFromAsset(itemView.context?.assets, "fonts/FZLanTingHeiS-L-GB-Regular.TTF")
        }

        fun bindData(bean: VideoBean, position: Int) {
            val context = itemView.context
            with(bean) {

                ImageLoadUtils.display(context!!, itemView?.iv_photo, feed!!)
                itemView?.tv_title?.text = title
                isDownload = SPUtils.getInstance(context, "download_state").getBoolean(playUrl!!)
                getDownloadState(context, playUrl!!, itemView)
                if (isDownload) {
                    itemView?.iv_download_state?.setImageResource(R.drawable.icon_download_stop)
                } else {
                    itemView?.iv_download_state?.setImageResource(R.drawable.icon_download_start)
                }

                itemView?.iv_download_state?.setOnClickListener {
                    if (isDownload) {
                        isDownload = false
                        SPUtils.getInstance(context, "download_state").put(playUrl!!, false)
                        itemView.iv_download_state?.setImageResource(R.drawable.icon_download_start)
                        RxDownload.getInstance(context).pauseServiceDownload(playUrl).subscribe()
                    } else {
                        isDownload = true
                        SPUtils.getInstance(context, "download_state").put(playUrl!!, true)
                        itemView.iv_download_state?.setImageResource(R.drawable.icon_download_stop)
                        addMission(context, playUrl, position + 1)
                    }
                }
                itemView.setOnClickListener { itemClick(this) }
            }
            itemView.setOnLongClickListener {
                itemLongClick(position)
                return@setOnLongClickListener true
            }
        }

        private fun getDownloadState(context: Context, playUrl: String, itemView: View?) {
            disposable = RxDownload.getInstance(context).receiveDownloadStatus(playUrl)
                    .subscribe { event ->
                        //当事件为Failed时, 才会有异常信息, 其余时候为null.
                        if (event.flag == DownloadFlag.FAILED) {
                            val throwable = event.error
                            Log.w("Error", throwable)
                        }
                        var downloadStatus = event.downloadStatus
                        var percent = downloadStatus.percentNumber
                        if (percent == 100L) {
                            if (!disposable.isDisposed && disposable != null) {
                                disposable.dispose()
                            }
                            hasLoaded = true
                            itemView?.iv_download_state?.visibility = View.GONE
                            itemView?.tv_detail?.text = "已缓存"
                            isDownload = false
                            SPUtils.getInstance(context, "download_state").put(playUrl.toString(), false)
                        } else {
                            if (itemView?.iv_download_state?.visibility != View.VISIBLE) {
                                itemView?.iv_download_state?.visibility = View.VISIBLE
                            }
                            if (isDownload) {
                                itemView?.tv_detail?.text = "缓存中 / $percent%"
                            } else {
                                itemView?.tv_detail?.text = "已暂停 / $percent%"
                            }
                        }
                    }
        }

        private fun addMission(context: Context, playUrl: String?, count: Int) {
            RxDownload.getInstance(context).serviceDownload(playUrl, "download$count").subscribe({
                Toast.makeText(context, "开始下载", Toast.LENGTH_SHORT).show()
            }, {
                Toast.makeText(context, "添加任务失败", Toast.LENGTH_SHORT).show()
            })
        }
    }


}