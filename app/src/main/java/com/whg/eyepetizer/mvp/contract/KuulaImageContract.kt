package com.whg.eyepetizer.mvp.contract

import com.whg.eyepetizer.base.BasePresenter
import com.whg.eyepetizer.base.BaseView
import com.whg.eyepetizer.mvp.model.bean.KuulaImageBean

/**
 * Created by guanhuawei on 2017/8/30.
 */
interface KuulaImageContract{
    interface View : BaseView<Presenter> {
        fun showContent(kuulaImageBean: KuulaImageBean)
    }
    interface Presenter : BasePresenter {
        fun getVrCategoryData(id:String)
    }
}