package com.whg.eyepetizer.fragment

import android.support.v4.app.Fragment
import com.tt.lvruheng.eyepetizer.ui.fragment.BaseFragment
import com.whg.eyepetizer.R
import com.whg.eyepetizer.adapter.NewHomeAdapter
import kotlinx.android.synthetic.main.new_home_fragment.*

/**
 * Created by guanhuawei on 2017/8/31.
 */
class HomeDetailFragment:BaseFragment(){


    override fun getLayoutResources(): Int {
     return R.layout.new_home_fragment
    }
    var mTabs = listOf<String>("新鲜", "鱼眼" ).toMutableList()
    lateinit var mFragments: ArrayList<Fragment>


    override fun initView() {
        var weekFragment: HomeFragment = HomeFragment()
        var monthFragment: VeerFragment = VeerFragment()

        mFragments = ArrayList()
        mFragments.clear()
        mFragments.add(weekFragment as Fragment)
        mFragments.add(monthFragment as Fragment)

        vp_content.adapter = NewHomeAdapter(fragmentManager, mFragments, mTabs)
        tabs.setupWithViewPager(vp_content)
    }
}