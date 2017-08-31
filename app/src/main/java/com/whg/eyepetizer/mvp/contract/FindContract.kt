package com.whg.eyepetizer.mvp.contract

import com.whg.eyepetizer.base.BasePresenter
import com.whg.eyepetizer.base.BaseView
import com.whg.eyepetizer.mvp.model.bean.FindBean

/**
 * Created by guanhuawei on 2017/8/30.
 */
interface FindContract{
    interface View : BaseView<Presenter> {
        fun setData(beans : ArrayList<FindBean>)
    }
    interface Presenter : BasePresenter {
        fun requestData()
    }
}