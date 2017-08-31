package com.whg.eyepetizer.mvp.presenter

import android.content.Context
import com.whg.eyepetizer.mvp.contract.FindContract
import com.whg.eyepetizer.mvp.model.FindModel
import com.whg.eyepetizer.mvp.model.bean.FindBean
import com.whg.eyepetizer.utils.applySchedulers
import io.reactivex.Observable

/**
 * Created by guanhuawei on 2017/8/30.
 */
class FindPresenter(context: Context, view: FindContract.View) : FindContract.Presenter {
    var mContext: Context? = null
    var mView: FindContract.View? = null
    val mModel: FindModel by lazy {
        FindModel()
    }

    init {
        mView = view
        mContext = context
    }

    override fun start() {
        requestData()
    }

    override fun requestData() {
        val observable: Observable<ArrayList<FindBean>>? = mContext?.let { mModel.loadData(mContext!!) }
        observable?.applySchedulers()?.subscribe {
            mView?.setData(it)
        }
    }


}