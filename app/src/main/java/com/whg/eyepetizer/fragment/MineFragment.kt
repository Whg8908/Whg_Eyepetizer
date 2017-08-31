package com.whg.eyepetizer.fragment

import android.graphics.Typeface
import android.view.View
import com.tt.lvruheng.eyepetizer.ui.fragment.BaseFragment
import com.whg.eyepetizer.R
import com.whg.eyepetizer.ui.AdviseActivity
import com.whg.eyepetizer.ui.CacheActivity
import com.whg.eyepetizer.ui.WatchActivity
import kotlinx.android.synthetic.main.mine_fragment.*
import org.jetbrains.anko.support.v4.startActivity

/**
 * Created by guanhuawei on 2017/8/28.
 */
class MineFragment : BaseFragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_watch ->{
                startActivity<WatchActivity>()
            }
            R.id.tv_advise ->{
                startActivity<AdviseActivity>()
            }
            R.id.tv_save ->{
                startActivity<CacheActivity>()
            }
        }
    }

    override fun getLayoutResources(): Int {
        return R.layout.mine_fragment
    }

    override fun initView() {
        tv_advise.setOnClickListener(this)
        tv_watch.setOnClickListener(this)
        tv_save.setOnClickListener(this)
        tv_advise.typeface = Typeface.createFromAsset(context?.assets, "fonts/FZLanTingHeiS-DB1-GB-Regular.TTF")
        tv_watch.typeface = Typeface.createFromAsset(context?.assets, "fonts/FZLanTingHeiS-DB1-GB-Regular.TTF")
        tv_save.typeface = Typeface.createFromAsset(context?.assets, "fonts/FZLanTingHeiS-DB1-GB-Regular.TTF")
    }

}