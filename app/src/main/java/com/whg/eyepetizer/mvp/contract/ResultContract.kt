package com.whg.eyepetizer.mvp.contract

import com.whg.eyepetizer.base.BasePresenter
import com.whg.eyepetizer.base.BaseView
import com.whg.eyepetizer.mvp.model.bean.HotBean

/**
 * Created by guanhuawei on 2017/8/29.
 */

interface ResultContract{
    interface View:BaseView<Presenter>{
        fun setData(bean: HotBean)
    }

    interface Presenter:BasePresenter{
        fun requestData(query:String,start:Int)
    }
}