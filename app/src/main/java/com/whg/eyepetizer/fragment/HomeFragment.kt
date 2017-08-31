package com.whg.eyepetizer.fragment

import android.os.Parcelable
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.tt.lvruheng.eyepetizer.ui.fragment.BaseFragment
import com.whg.eyepetizer.R
import com.whg.eyepetizer.adapter.HomeAdapter
import com.whg.eyepetizer.mvp.contract.HomeContract
import com.whg.eyepetizer.mvp.model.bean.HomeBean
import com.whg.eyepetizer.mvp.model.bean.VideoBean
import com.whg.eyepetizer.mvp.presenter.HomePresenter
import com.whg.eyepetizer.ui.VideoDetailActivity
import com.whg.eyepetizer.utils.ObjectSaveUtils
import com.whg.eyepetizer.utils.SPUtils
import kotlinx.android.synthetic.main.home_fragment.*
import org.jetbrains.anko.startActivity
import java.util.regex.Pattern


/**
 * Created by guanhuawei on 2017/8/28.
 */
class HomeFragment : BaseFragment(), HomeContract.View, SwipeRefreshLayout.OnRefreshListener {

    var mIsRefresh: Boolean = false
    var mPresenter: HomePresenter? = null
    var mList = ArrayList<HomeBean.IssueListBean.ItemListBean>()
    var mAdapter: HomeAdapter? = null
    var data: String? = null


    override fun setData(bean: HomeBean) {
        val regEx = "[^0-9]"
        val p = Pattern.compile(regEx)
        val m = p.matcher(bean.nextPageUrl)
        data = m.replaceAll("").subSequence(1, m.replaceAll("").length - 1).toString()
        if (mIsRefresh) {
            mIsRefresh = false
            refreshLayout.isRefreshing = false
            if (mList.size > 0) {
                mList.clear()
            }

        }
        bean.issueList!!
                .flatMap { it.itemList!! }
                .filter { it.type.equals("video") }
                .forEach { mList.add(it) }
        mAdapter?.notifyDataSetChanged()
    }


    override fun getLayoutResources(): Int {
        return R.layout.home_fragment
    }

    override fun initView() {
        mPresenter = HomePresenter(context, this)

        recyclerView.layoutManager = LinearLayoutManager(context)
        mAdapter = HomeAdapter(mList) {
            var videoBean = VideoBean(it.cover?.feed, it.title, it.description, it.duration, it.playUrl, it.category, it.cover?.blurred, it.consumption?.collectionCount, it.consumption?.shareCount, it.consumption?.replyCount, System.currentTimeMillis())
            var url = SPUtils.getInstance(activity, "beans").getString(it.playUrl)
            if (url.equals("")) {
                var count = SPUtils.getInstance(activity, "beans").getInt("count")
                if (count != -1) {
                    count = count.inc()
                } else {
                    count = 1
                }
                SPUtils.getInstance(activity!!, "beans").put("count", count)
                SPUtils.getInstance(activity!!, "beans").put("playUrl", it.playUrl)
                ObjectSaveUtils.saveObject(activity!!, "bean$count", videoBean)
            }
            activity.startActivity<VideoDetailActivity>("data" to videoBean as Parcelable)
        }
        recyclerView.adapter = mAdapter
        refreshLayout.setOnRefreshListener(this)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                var layoutManager: LinearLayoutManager = recyclerView?.layoutManager as LinearLayoutManager
                var lastPositon = layoutManager.findLastVisibleItemPosition()
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastPositon == mList.size - 1) {
                    if (data != null) {
                        mPresenter?.moreData(data)
                    }

                }
            }
        })
        mPresenter?.start()
    }

    override fun onRefresh() {
        if (!mIsRefresh) {
            mIsRefresh = true
            mPresenter?.start()
        }
    }
}