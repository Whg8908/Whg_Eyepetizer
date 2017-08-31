package com.whg.eyepetizer.mvp.presenter

import android.content.Context
import com.whg.eyepetizer.mvp.contract.KuulaImageContract
import com.whg.eyepetizer.mvp.model.KuulaIamgeModel
import com.whg.eyepetizer.mvp.model.bean.KuulaImageBean
import com.whg.eyepetizer.utils.applySchedulers
import io.reactivex.Observable

/**
 * Created by guanhuawei on 2017/8/30.
 */
class KuulaImagePresenter(context: Context, view: KuulaImageContract.View) : KuulaImageContract.Presenter {


    var mContext: Context? = null
    var mView: KuulaImageContract.View? = null
    val mModel: KuulaIamgeModel by lazy {
        KuulaIamgeModel()
    }

    init {
        mView = view
        mContext = context
    }

    override fun start() {

    }

    override fun getVrCategoryData(id: String) {
        val observable: Observable<KuulaImageBean>? = mContext?.let {
            mModel.loadData(mContext!!, id)
        }
        observable?.applySchedulers()?.subscribe { bean: KuulaImageBean ->
            mView?.showContent(bean)
        }
    }


}