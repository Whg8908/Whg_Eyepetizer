package com.whg.eyepetizer.ui

import android.os.Parcelable
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import com.whg.eyepetizer.R
import com.whg.eyepetizer.adapter.FeedAdapter
import com.whg.eyepetizer.base.BaseActivity
import com.whg.eyepetizer.mvp.contract.ResultContract
import com.whg.eyepetizer.mvp.model.bean.HotBean
import com.whg.eyepetizer.mvp.presenter.ResultPresenter
import com.whg.eyepetizer.utils.CVideoBeanUtils
import kotlinx.android.synthetic.main.activity_result.*
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity

/**
 * Created by guanhuawei on 2017/8/29.
 */
class ResultActivity : BaseActivity(), ResultContract.View, SwipeRefreshLayout.OnRefreshListener {

    override val isFullScreen: Boolean = false

    override val toolbar by lazy { find<Toolbar>(R.id.toolbar) }

    lateinit var keyWord: String
    lateinit var mPresenter: ResultPresenter
    lateinit var mAdapter: FeedAdapter
    var mIsRefresh: Boolean = false
    var mList = ArrayList<HotBean.ItemListBean.DataBean>()
    var start: Int = 10


    override fun getLayoutId(): Int {
        return R.layout.activity_result
    }

    override fun initPresenter() {
        mPresenter = ResultPresenter(this, this)
    }

    override fun initView() {
        keyWord = intent.getStringExtra("keyWord")
        setToolBar()
        initRecyclerView()
        initData()
    }


    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = FeedAdapter(mList) {
            //跳转视频详情页
            var videoBean = CVideoBeanUtils.setVideoBean(this, it)
            startActivity<VideoDetailActivity>("data" to videoBean as Parcelable)
        }

        recyclerView.adapter = mAdapter
        refreshLayout.setOnRefreshListener(this)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                var layoutManager: LinearLayoutManager = recyclerView?.layoutManager as LinearLayoutManager
                var lastPositon = layoutManager.findLastVisibleItemPosition()
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastPositon == mList.size - 1) {
                    start = start.plus(10)
                    mPresenter.requestData(keyWord, start)
                }
            }
        })
    }

    private fun initData() {
        mPresenter.requestData(keyWord, start)
    }

    private fun setToolBar() {
        toolbarTitle = "'$keyWord' 相关"
        enableHomeAsUp { onBackPressed() }
    }

    override fun setData(bean: HotBean) {
        if (mIsRefresh) {
            mIsRefresh = false
            refreshLayout.isRefreshing = false
            if (mList.size > 0) {
                mList.clear()
            }
        }
        bean.itemList?.forEach {
            it.data?.let { it1 -> mList.add(it1) }
        }
        mAdapter.notifyDataSetChanged()
    }

    override fun onRefresh() {
        if (!mIsRefresh) {
            mIsRefresh = true
            start = 10
            mPresenter.requestData(keyWord, start)
        }
    }
}