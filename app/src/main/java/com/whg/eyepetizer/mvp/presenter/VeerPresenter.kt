package com.whg.eyepetizer.mvp.presenter

import android.content.Context
import com.whg.eyepetizer.mvp.contract.VeerContract
import com.whg.eyepetizer.mvp.model.VeerModel
import com.whg.eyepetizer.mvp.model.bean.KuulaListBean
import com.whg.eyepetizer.utils.applySchedulers
import io.reactivex.Observable

/**
 * Created by guanhuawei on 2017/8/30.
 */

class VeerPresenter(context: Context, view: VeerContract.View) : VeerContract.Presenter {

    var mContext: Context? = null
    var mView: VeerContract.View? = null
    val mModel: VeerModel by lazy {
        VeerModel()
    }

    init {
        mView = view
        mContext = context
    }

    override fun start() {
    }


    override fun getVrCategoryData(offset: Int) {
        val observable: Observable<KuulaListBean>? = mContext?.let {
            mModel.getVrCategoryData(mContext!!, offset)
        }
        observable?.applySchedulers()?.subscribe { homeBean: KuulaListBean ->
            mView?.showContent(homeBean)
        }
    }
}