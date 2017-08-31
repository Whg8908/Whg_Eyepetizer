package com.whg.eyepetizer.mvp.model

import android.content.Context
import com.whg.eyepetizer.mvp.model.bean.ApiService
import com.whg.eyepetizer.mvp.model.bean.HotBean
import com.whg.eyepetizer.mvp.model.bean.RetrofitClient
import io.reactivex.Observable

/**
 * Created by guanhuawei on 2017/8/30.
 */
class HotModel{
    fun loadData(context: Context, strategy: String?): Observable<HotBean>? {
        val retrofitClient = RetrofitClient.getInstance(context, ApiService.BASE_URL)
        val apiService  = retrofitClient.create(ApiService::class.java)
        return apiService?.getHotData(10, strategy!!,"26868b32e808498db32fd51fb422d00175e179df",83)

    }
}