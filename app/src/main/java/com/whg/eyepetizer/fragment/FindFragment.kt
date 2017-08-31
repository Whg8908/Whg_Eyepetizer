package com.whg.eyepetizer.fragment

import android.support.v7.widget.GridLayoutManager
import com.tt.lvruheng.eyepetizer.ui.fragment.BaseFragment
import com.whg.eyepetizer.R
import com.whg.eyepetizer.adapter.FindAdapter
import com.whg.eyepetizer.mvp.contract.FindContract
import com.whg.eyepetizer.mvp.model.bean.FindBean
import com.whg.eyepetizer.mvp.presenter.FindPresenter
import com.whg.eyepetizer.ui.FindDetailActivity
import kotlinx.android.synthetic.main.find_fragment.*
import org.jetbrains.anko.startActivity


/**
 * Created by guanhuawei on 2017/8/28.
 */
class FindFragment : BaseFragment(), FindContract.View {

    var mPresenter: FindPresenter? = null
    var mAdapter: FindAdapter? = null
    var mList = ArrayList<FindBean>()

    override fun setData(beans: ArrayList<FindBean>) {
        mList.clear()
        mList.addAll(beans)
        mAdapter?.notifyDataSetChanged()
    }

    override fun getLayoutResources(): Int {
        return R.layout.find_fragment
    }

    override fun initView() {
        mPresenter = FindPresenter(context, this)

        mAdapter = FindAdapter(mList) {
              activity.startActivity<FindDetailActivity>("name" to it.name as String)
        }
        gv_find.layoutManager = GridLayoutManager(this@FindFragment.activity, 2)
        gv_find.adapter = mAdapter

        mPresenter?.start()
    }

}