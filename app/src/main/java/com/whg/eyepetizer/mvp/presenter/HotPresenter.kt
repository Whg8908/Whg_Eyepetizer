package com.whg.eyepetizer.mvp.presenter

import android.content.Context
import com.whg.eyepetizer.mvp.contract.HotContract
import com.whg.eyepetizer.mvp.model.HotModel
import com.whg.eyepetizer.mvp.model.bean.HotBean
import com.whg.eyepetizer.utils.applySchedulers
import io.reactivex.Observable

/**
 * Created by guanhuawei on 2017/8/30.
 */
class HotPresenter(context: Context, view: HotContract.View) : HotContract.Presenter{


    var mContext : Context? = null
    var mView : HotContract.View? = null
    val mModel : HotModel by lazy {
        HotModel()
    }
    init {
        mView = view
        mContext = context
    }
    override fun start() {

    }
    override fun requestData(strategy: String) {
        val observable : Observable<HotBean>? = mContext?.let { mModel.loadData(mContext!!,strategy) }
        observable?.applySchedulers()?.subscribe {
            mView?.setData(it)
        }
    }

}