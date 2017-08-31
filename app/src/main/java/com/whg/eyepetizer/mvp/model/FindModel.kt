package com.whg.eyepetizer.mvp.model

import android.content.Context
import com.whg.eyepetizer.mvp.model.bean.ApiService
import com.whg.eyepetizer.mvp.model.bean.FindBean
import com.whg.eyepetizer.mvp.model.bean.RetrofitClient
import io.reactivex.Observable

/**
 * Created by guanhuawei on 2017/8/30.
 */
class FindModel() {
    fun loadData(context: Context): Observable<ArrayList<FindBean>>? {
        val retrofitClient = RetrofitClient.getInstance(context, ApiService.BASE_URL)
        val apiService = retrofitClient.create(ApiService::class.java)
        return apiService?.getFindData()
    }
}