package com.whg.eyepetizer.mvp.contract

import android.content.Context
import com.whg.eyepetizer.mvp.model.bean.ApiService
import com.whg.eyepetizer.mvp.model.bean.HomeBean
import com.whg.eyepetizer.mvp.model.bean.RetrofitClient
import io.reactivex.Observable

/**
 * Created by guanhuawei on 2017/8/30.
 */
class HomeModel{
    fun loadData(context: Context,isFirst: Boolean,data: String?): Observable<HomeBean>? {
        val retrofitClient = RetrofitClient.getInstance(context,ApiService.BASE_URL)
        val apiService  = retrofitClient.create(ApiService::class.java)
        when(isFirst) {
            true -> return apiService?.getHomeData()

            false -> return apiService?.getHomeMoreData(data.toString(), "2")
        }
    }
}