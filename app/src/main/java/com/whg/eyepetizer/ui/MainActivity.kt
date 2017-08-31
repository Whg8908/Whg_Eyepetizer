package com.whg.eyepetizer.ui

import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import com.whg.eyepetizer.R
import com.whg.eyepetizer.base.BaseActivity
import com.whg.eyepetizer.fragment.*
import com.whg.eyepetizer.search.SEARCH_TAG
import com.whg.eyepetizer.search.SearchFragment
import com.whg.eyepetizer.utils.showToast
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.find
import java.util.*

/**
 * 主页面
 */
class MainActivity : BaseActivity(), View.OnClickListener {
    override val toolbar by lazy { find<Toolbar>(R.id.toolbar) }
    override val isFullScreen: Boolean = false


    var homeFragment: HomeDetailFragment? = null
    var findFragment: FindFragment? = null
    var hotFragment: HotFragment? = null
    var mineFragment: MineFragment? = null
    lateinit var searchFragment: SearchFragment
    var mExitTime: Long = 0
    var toast: Toast? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initFragment(savedInstanceState)
    }



    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initPresenter() {

    }

    override fun initView() {
        window.attributes.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        setRadioButton()
        initToolbar()
    }

    private fun initFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val fragments = supportFragmentManager.fragments

            for (fragment in fragments) {
                if (fragment is HomeDetailFragment) {
                    homeFragment = fragment
                }
                if (fragment is FindFragment) {
                    findFragment = fragment
                }
                if (fragment is HotFragment) {
                    hotFragment = fragment
                }
                if (fragment is MineFragment) {
                    mineFragment = fragment
                }
            }
        } else {
            homeFragment = HomeDetailFragment()
            findFragment = FindFragment()
            mineFragment = MineFragment()
            hotFragment = HotFragment()
            val fragmentTrans = supportFragmentManager.beginTransaction()
            fragmentTrans.add(R.id.fl_content, homeFragment)
            fragmentTrans.add(R.id.fl_content, findFragment)
            fragmentTrans.add(R.id.fl_content, mineFragment)
            fragmentTrans.add(R.id.fl_content, hotFragment)
            fragmentTrans.commit()
        }
        supportFragmentManager.beginTransaction().show(homeFragment)
                .hide(findFragment)
                .hide(mineFragment)
                .hide(hotFragment)
                .commit()
    }

    private fun initToolbar() {
        var today = getToday()
        tv_bar_title.text = today
        tv_bar_title.typeface = Typeface.createFromAsset(this.assets, "fonts/Lobster-1.4.otf")
        iv_search.setOnClickListener {
            if (rb_mine.isChecked) {

            } else {
                searchFragment = SearchFragment()
                searchFragment.show(fragmentManager, SEARCH_TAG)
            }
        }
    }

    private fun setRadioButton() {
        rb_home.isChecked = true
        rb_home.setTextColor(resources.getColor(R.color.black))
        rb_home.setOnClickListener(this)
        rb_find.setOnClickListener(this)
        rb_hot.setOnClickListener(this)
        rb_mine.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        clearState()

        when (v?.id) {
            R.id.rb_home -> {
                rb_home.isChecked = true
                rb_home.setTextColor(resources.getColor(R.color.black))
                supportFragmentManager.beginTransaction()
                        .show(homeFragment)
                        .hide(findFragment)
                        .hide(hotFragment)
                        .hide(mineFragment)
                        .commit()
                tv_bar_title.text = getToday()
                tv_bar_title.visibility = View.VISIBLE
                iv_search.setImageResource(R.drawable.icon_search)
            }
            R.id.rb_find -> {
                rb_find.isChecked = true
                rb_find.setTextColor(resources.getColor(R.color.black))
                supportFragmentManager.beginTransaction()
                        .show(findFragment)
                        .hide(homeFragment)
                        .hide(hotFragment)
                        .hide(mineFragment)
                        .commit()
                tv_bar_title.text = "Discover"
                tv_bar_title.visibility = View.VISIBLE
                iv_search.setImageResource(R.drawable.icon_search)
            }
            R.id.rb_hot -> {
                rb_hot.isChecked = true
                rb_hot.setTextColor(resources.getColor(R.color.black))
                supportFragmentManager.beginTransaction()
                        .show(hotFragment)
                        .hide(homeFragment)
                        .hide(findFragment)
                        .hide(mineFragment)
                        .commit()
                tv_bar_title.text = "Ranking"
                tv_bar_title.visibility = View.VISIBLE
                iv_search.setImageResource(R.drawable.icon_search)
            }
            R.id.rb_mine -> {
                rb_mine.isChecked = true
                rb_mine.setTextColor(resources.getColor(R.color.black))
                supportFragmentManager.beginTransaction()
                        .show(mineFragment)
                        .hide(homeFragment)
                        .hide(findFragment)
                        .hide(hotFragment)
                        .commit()
                tv_bar_title.visibility = View.INVISIBLE
                iv_search.setImageResource(R.drawable.icon_setting)
            }
        }
    }

    private fun getToday(): String? {
        var list = arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
        var data: Date = Date()
        var calendar: Calendar = Calendar.getInstance()
        calendar.time = data
        var index: Int = calendar.get(Calendar.DAY_OF_WEEK) - 1
        if (index < 0) {
            index = 0
        }
        return list[index]
    }

    private fun clearState() {
        rg_root.clearCheck()
        rb_home.setTextColor(resources.getColor(R.color.gray))
        rb_find.setTextColor(resources.getColor(R.color.gray))
        rb_hot.setTextColor(resources.getColor(R.color.gray))
        rb_mine.setTextColor(resources.getColor(R.color.gray))
    }

    override fun onPause() {
        super.onPause()
        toast?.let { toast!!.cancel() }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis().minus(mExitTime) <= 3000) {
                finish()
                toast!!.cancel()
            } else {
                mExitTime = System.currentTimeMillis()
                toast = showToast("再按一次退出程序")
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
