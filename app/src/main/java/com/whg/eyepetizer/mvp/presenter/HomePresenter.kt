package com.whg.eyepetizer.mvp.presenter

import android.content.Context
import com.whg.eyepetizer.mvp.contract.HomeContract
import com.whg.eyepetizer.mvp.contract.HomeModel
import com.whg.eyepetizer.mvp.model.bean.HomeBean
import com.whg.eyepetizer.utils.applySchedulers
import io.reactivex.Observable

/**
 * Created by guanhuawei on 2017/8/29.
 */
class HomePresenter(context: Context,view : HomeContract.View) : HomeContract.Presenter{

    var mContext : Context? = null
    var mView : HomeContract.View? = null
    val mModel : HomeModel by lazy {
        HomeModel()
    }
    init {
        mView = view
        mContext = context
    }
    override fun start() {
        requestData()
    }

    override fun requestData() {
        val observable : Observable<HomeBean>? = mContext?.let { mModel.loadData(it,true,"0") }
        observable?.applySchedulers()?.subscribe { homeBean : HomeBean ->
            mView?.setData(homeBean)
        }
    }
    fun moreData(data: String?){
        val observable : Observable<HomeBean>? = mContext?.let { mModel.loadData(it,false,data) }
        observable?.applySchedulers()?.subscribe { homeBean : HomeBean ->
            mView?.setData(homeBean)
        }
    }


}