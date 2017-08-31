package com.whg.eyepetizer.ui

import android.os.Parcelable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import com.whg.eyepetizer.R
import com.whg.eyepetizer.adapter.WatchAdapter
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

/**
 * Created by guanhuawei on 2017/8/30.
 */
class WatchActivity : BaseActivity() {
    override val isFullScreen: Boolean = false
    override val toolbar by lazy { find<Toolbar>(R.id.toolbar) }

    var mList = ArrayList<VideoBean>()
    lateinit var mAdapter: WatchAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_watch
    }

    override fun initPresenter() {
    }

    override fun initView() {
        setToolbar()
        initRecyclerView()
        getWatchData()
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = WatchAdapter(mList) {
            var url = SPUtils.getInstance(this, "beans").getString(it.playUrl!!)
            if (url.equals("")) {
                var count = SPUtils.getInstance(this!!, "beans").getInt("count")
                if (count != -1) {
                    count = count.inc()
                } else {
                    count = 1
                }
                SPUtils.getInstance(this!!, "beans").put("count", count)
                SPUtils.getInstance(this!!, "beans").put("playUrl"!!, it.playUrl!!)
                ObjectSaveUtils.saveObject(this!!, "bean$count", it)
            }
            startActivity<VideoDetailActivity>("data" to it as Parcelable)
        }
        recyclerView.adapter = mAdapter
    }

    private fun getWatchData() = async(UI) {
        val result = bg {
            getDataList()
        }
        updateUI(result.await())
    }

    private fun updateUI(datas: ArrayList<VideoBean>) {
        if (datas?.size?.compareTo(0) == 0) {
            tv_hint.visibility = View.VISIBLE
        } else {
            tv_hint.visibility = View.GONE
            if (mList.size > 0) {
                mList.clear()
            }
            datas?.let { mList.addAll(it) }
            mAdapter.notifyDataSetChanged()
        }
    }

    private fun getDataList(): ArrayList<VideoBean> {
        var list = ArrayList<VideoBean>()
        var count: Int = SPUtils.getInstance(this, "beans").getInt("count")
        var i = 1
        while (i.compareTo(count) <= 0) {
            var bean: VideoBean = ObjectSaveUtils.getValue(this, "bean$i") as VideoBean
            list.add(bean)
            i++
        }
        return list
    }


    private fun setToolbar() {
        toolbarTitle = "观看记录"
        enableHomeAsUp { onBackPressed() }
    }


}