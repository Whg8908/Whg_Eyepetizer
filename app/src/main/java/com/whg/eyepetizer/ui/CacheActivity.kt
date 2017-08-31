package com.whg.eyepetizer.ui

import android.net.Uri
import android.os.Parcelable
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import com.whg.eyepetizer.R
import com.whg.eyepetizer.adapter.DownloadAdapter
import com.whg.eyepetizer.base.BaseActivity
import com.whg.eyepetizer.mvp.model.bean.VideoBean
import com.whg.eyepetizer.utils.ObjectSaveUtils
import com.whg.eyepetizer.utils.SPUtils
import kotlinx.android.synthetic.main.activity_watch.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import zlc.season.rxdownload2.RxDownload

/**
 * Created by guanhuawei on 2017/8/30.
 */
class CacheActivity : BaseActivity() {

    override val isFullScreen: Boolean = false

    override val toolbar by lazy { find<Toolbar>(R.id.toolbar) }

    var mList = ArrayList<VideoBean>()
    lateinit var mAdapter: DownloadAdapter


    override fun getLayoutId(): Int {
        return R.layout.activity_watch
    }

    override fun initPresenter() {
    }

    override fun initView() {
        setToolbar()
        getData()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = DownloadAdapter(mList,
                itemClick = {
                    //跳转视频详情页
                    intent.putExtra("data", it as Parcelable)
                    if (mAdapter.viewHolder?.hasLoaded) {
                        var files = RxDownload.getInstance(this).getRealFiles(it.playUrl)
                        var uri = Uri.fromFile(files!![0])
                        intent.putExtra("loaclFile", uri.toString())
                    }
                    startActivity<VideoDetailActivity>("data" to it as Parcelable)
                }, itemLongClick = { addDialog(it) })


        recyclerView.adapter = mAdapter
    }


    private fun getData() = async(UI) {
        val result = bg {
            getDataList()
        }
        updateUI(result.await())
    }

    private fun getDataList(): ArrayList<VideoBean> {
        var list = ArrayList<VideoBean>()
        var count: Int = SPUtils.getInstance(this, "downloads").getInt("count")
        var i = 1
        while (i.compareTo(count) <= 0) {
            var bean: VideoBean
            if (ObjectSaveUtils.getValue(this, "download$i") == null) {
                continue
            } else {
                bean = ObjectSaveUtils.getValue(this, "download$i") as VideoBean
            }
            list.add(bean)
            i++
        }
        return list
    }

    private fun updateUI(list: ArrayList<VideoBean>) {
        if (list?.size.compareTo(0) == 0) {
            tv_hint.visibility = View.VISIBLE
        } else {
            tv_hint.visibility = View.GONE
            if (mList.size > 0) {
                mList.clear()
            }
            list?.let { mList.addAll(it) }
            mAdapter.notifyDataSetChanged()
        }
    }

    private fun addDialog(position: Int) {
        var builder = AlertDialog.Builder(this)
        var dialog = builder.create()
        builder.setMessage("是否删除当前视频")
        builder.setNegativeButton("否", {
            dialog, which ->
            dialog.dismiss()
        })
        builder.setPositiveButton("是", {
            dialog, which ->
            deleteDownload(position)
        })
        builder.show()
    }

    private fun deleteDownload(position: Int) {
        RxDownload.getInstance(this@CacheActivity).deleteServiceDownload(mList[position].playUrl, true).subscribe()
        SPUtils.getInstance(this, "downloads").put(mList[position].playUrl.toString(), "")
        var count = position + 1
        ObjectSaveUtils.deleteFile("download$count", this)
        mList.removeAt(position)
        mAdapter.notifyItemRemoved(position)
    }

    private fun setToolbar() {

        toolbarTitle = "我的缓存"
        enableHomeAsUp { onBackPressed() }
    }

}