package com.whg.eyepetizer.mvp.contract

import com.whg.eyepetizer.base.BasePresenter
import com.whg.eyepetizer.base.BaseView
import com.whg.eyepetizer.mvp.model.bean.KuulaListBean

/**
 * Created by guanhuawei on 2017/8/30.
 */
interface VeerContract{

    interface View : BaseView<Presenter> {
        fun showContent(kuulaListBean: KuulaListBean)
    }

    interface Presenter : BasePresenter {
        fun getVrCategoryData(offset:Int)
    }
}