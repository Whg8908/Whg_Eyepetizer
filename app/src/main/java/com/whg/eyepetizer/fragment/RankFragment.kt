package com.whg.eyepetizer.fragment

import android.os.Parcelable
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.tt.lvruheng.eyepetizer.ui.fragment.BaseFragment
import com.whg.eyepetizer.R
import com.whg.eyepetizer.adapter.RankAdapter
import com.whg.eyepetizer.mvp.contract.HotContract
import com.whg.eyepetizer.mvp.model.bean.HotBean
import com.whg.eyepetizer.mvp.model.bean.VideoBean
import com.whg.eyepetizer.mvp.presenter.HotPresenter
import com.whg.eyepetizer.ui.VideoDetailActivity
import com.whg.eyepetizer.utils.ObjectSaveUtils
import com.whg.eyepetizer.utils.SPUtils
import kotlinx.android.synthetic.main.home_fragment.*
import org.jetbrains.anko.startActivity

/**
 * Created by guanhuawei on 2017/8/30.
 */
class RankFragment : BaseFragment(), HotContract.View {
    lateinit var mPresenter: HotPresenter
    lateinit var mStrategy: String
    lateinit var mAdapter: RankAdapter
    var mList: ArrayList<HotBean.ItemListBean.DataBean> = ArrayList()
    override fun getLayoutResources(): Int {
        return R.layout.rank_fragment
    }

    override fun initView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        mAdapter = RankAdapter(mList){

            //跳转视频详情页
            var videoBean  = VideoBean(it.cover?.feed,it.title,it.description,it.duration,it.playUrl,it.category,it.cover?.blurred,it.consumption?.collectionCount ,it.consumption?.shareCount ,it.consumption?.replyCount, System.currentTimeMillis())
            var url = SPUtils.getInstance(context!!,"beans").getString(it.playUrl!!)
            if(url.equals("")){
                var count = SPUtils.getInstance(context!!,"beans").getInt("count")
                if(count!=-1){
                    count = count.inc()
                }else{
                    count = 1
                }
                SPUtils.getInstance(context!!,"beans").put("count",count)
                SPUtils.getInstance(context!!,"beans").put("playUrl",it.playUrl!!)
                ObjectSaveUtils.saveObject(context!!,"bean$count",videoBean)
            }
            activity.startActivity<VideoDetailActivity>("data" to videoBean as Parcelable)
        }
        recyclerView.adapter = mAdapter
        if (arguments != null) {
            mStrategy = arguments.getString("strategy")
            mPresenter = HotPresenter(context, this)
            mPresenter.requestData(mStrategy)
        }

    }

    override fun setData(bean: HotBean) {
        Log.e("rank", bean.toString())
        if(mList.size>0){
            mList.clear()
        }
        bean.itemList?.forEach {
            it.data?.let { it1 -> mList.add(it1) }
        }
        mAdapter.notifyDataSetChanged()
    }

}