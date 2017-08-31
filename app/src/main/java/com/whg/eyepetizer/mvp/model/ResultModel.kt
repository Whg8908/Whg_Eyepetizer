package com.whg.eyepetizer.mvp.contract

import android.content.Context
import com.whg.eyepetizer.mvp.model.bean.ApiService
import com.whg.eyepetizer.mvp.model.bean.HotBean
import com.whg.eyepetizer.mvp.model.bean.RetrofitClient
import io.reactivex.Observable

/**
 * Created by guanhuawei on 2017/8/30.
 */
class ResultModel {
    fun loadData(context: Context, query: String, start: Int): Observable<HotBean>? {
        val retrofitClient = RetrofitClient.getInstance(context, ApiService.BASE_URL)
        val apiService = retrofitClient.create(ApiService::class.java)
        return apiService?.getSearchData(10, query, start)
    }
}
