package com.whg.eyepetizer.mvp.contract

import com.whg.eyepetizer.base.BasePresenter
import com.whg.eyepetizer.base.BaseView
import com.whg.eyepetizer.mvp.model.bean.HomeBean

/**
 * Created by guanhuawei on 2017/8/29.
 */
interface HomeContract{
    interface View : BaseView<Presenter> {
        fun setData(bean : HomeBean)
    }
    interface Presenter : BasePresenter {
        fun requestData()
    }
}