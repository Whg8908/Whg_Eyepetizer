package com.whg.eyepetizer.ui

import android.os.Parcelable
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import com.whg.eyepetizer.R
import com.whg.eyepetizer.adapter.RankAdapter
import com.whg.eyepetizer.base.BaseActivity
import com.whg.eyepetizer.mvp.contract.FindDetailContract
import com.whg.eyepetizer.mvp.model.bean.HotBean
import com.whg.eyepetizer.mvp.presenter.FindDetailPresenter
import com.whg.eyepetizer.utils.CVideoBeanUtils
import kotlinx.android.synthetic.main.activity_find_detail.*
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import java.util.regex.Pattern

/**
 * Created by guanhuawei on 2017/8/30.
 */
class FindDetailActivity : BaseActivity(), FindDetailContract.View, SwipeRefreshLayout.OnRefreshListener {

    override val isFullScreen: Boolean = false

    override val toolbar by lazy { find<Toolbar>(R.id.toolbar) }

    lateinit var mPresenter: FindDetailPresenter
    lateinit var mAdapter: RankAdapter
    lateinit var data: String
    var mIsRefresh: Boolean = false
    var mList: ArrayList<HotBean.ItemListBean.DataBean> = ArrayList()
    var mstart: Int = 10
    lateinit var name: String

    override fun getLayoutId(): Int {
        return  R.layout.activity_find_detail
    }

    override fun initPresenter() {
        mPresenter = FindDetailPresenter(this, this)
    }

    override fun initView() {
        setToolbar()
        initRecyclerView()
        mPresenter.requestData(name, "date")
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = RankAdapter(mList) {
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
                    if (data != null) {
                        mPresenter?.requesMoreData(mstart, name, "date")
                        mstart = mstart.plus(10)
                    }

                }
            }
        })

    }

    private fun setToolbar() {
        intent.getStringExtra("name")?.let {
            name = intent.getStringExtra("name")
            toolbarTitle = name
        }
        enableHomeAsUp { onBackPressed() }
    }

    override fun onRefresh() {
        if (!mIsRefresh) {
            mIsRefresh = true
            mPresenter.requestData(name, "date")
        }
    }

    override fun setData(bean: HotBean) {
        val regEx = "[^0-9]"
        val p = Pattern.compile(regEx)
        val m = p.matcher(bean.nextPageUrl as CharSequence?)
        data = m.replaceAll("").subSequence(1, m.replaceAll("").length - 1).toString()
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
}
