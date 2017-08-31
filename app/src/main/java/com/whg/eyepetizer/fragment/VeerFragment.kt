package com.whg.eyepetizer.fragment

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.tt.lvruheng.eyepetizer.ui.fragment.BaseFragment
import com.whg.eyepetizer.R
import com.whg.eyepetizer.adapter.KuulaAdapter
import com.whg.eyepetizer.mvp.contract.VeerContract
import com.whg.eyepetizer.mvp.model.bean.KuulaListBean
import com.whg.eyepetizer.mvp.presenter.VeerPresenter
import com.whg.eyepetizer.ui.VrActivity
import kotlinx.android.synthetic.main.veer_fragment.*
import org.jetbrains.anko.startActivity
import java.util.*

/**
 * Created by guanhuawei on 2017/8/30.
 */
class VeerFragment : BaseFragment(), VeerContract.View {


    lateinit var mKuulaList: ArrayList<KuulaListBean.PayloadBean.PostsBean>
    lateinit var adapter: KuulaAdapter
    var pager = 0
    lateinit var mPresenter: VeerPresenter


    override fun getLayoutResources(): Int {
        return R.layout.veer_fragment
    }


    override fun initView() {
        mPresenter = VeerPresenter(activity, this)

        initRecyclerView()
        getDate(pager)
    }

    private fun initRecyclerView() {
        refresh.setProgressViewOffset(false, 100, 200)
        refresh.setOnRefreshListener {
            if (!mIsRefresh) {
                mIsRefresh = true
                pager = 0
                getDate(pager)
            }
        }
        val layout = GridLayoutManager(activity, 2)
        recycler.layoutManager = layout
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                var layoutManager: LinearLayoutManager = recyclerView?.layoutManager as LinearLayoutManager
                var lastPositon = layoutManager.findLastVisibleItemPosition()
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastPositon == mKuulaList.size - 1) {
                    getDate(pager)
                }
            }
        })

        mKuulaList = ArrayList<KuulaListBean.PayloadBean.PostsBean>()

        adapter = KuulaAdapter(mKuulaList) {
            activity.startActivity<VrActivity>("id" to it.id)
        }
        recycler.adapter = adapter

    }


    fun getDate(index: Int) {
        mPresenter.getVrCategoryData(index)
    }


    var mIsRefresh: Boolean = false

    override fun showContent(kuulaListBean: KuulaListBean) {
        if (mIsRefresh) {
            mIsRefresh = false
            refresh.isRefreshing = false
            if (mKuulaList.size > 0) {
                mKuulaList.clear()
            }

        }
        mKuulaList.addAll(kuulaListBean.payload.posts)
        adapter.notifyDataSetChanged()
        pager++
    }
}