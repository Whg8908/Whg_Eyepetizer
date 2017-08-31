package com.whg.eyepetizer.mvp.presenter

import android.content.Context
import com.whg.eyepetizer.mvp.contract.ResultContract
import com.whg.eyepetizer.mvp.contract.ResultModel
import com.whg.eyepetizer.mvp.model.bean.HotBean
import com.whg.eyepetizer.utils.applySchedulers
import io.reactivex.Observable

/**
 * Created by guanhuawei on 2017/8/29.
 */
class ResultPresenter(context: Context, view: ResultContract.View) : ResultContract.Presenter {

    var mContext: Context? = null
    var mView:ResultContract.View? = null

    val mModel: ResultModel by lazy {
        ResultModel()
    }

    init {
        mView = view
        mContext = context
    }

    override fun start() {

    }

    override fun requestData(query: String, start: Int) {
        val observable: Observable<HotBean>? = mContext?.let { mModel.loadData(mContext!!, query, start) }
        observable?.applySchedulers()?.subscribe { bean: HotBean ->
            mView?.setData(bean)
        }
    }

}