package com.whg.eyepetizer.mvp.model

import android.content.Context
import com.whg.eyepetizer.mvp.model.bean.ApiService
import com.whg.eyepetizer.mvp.model.bean.KuulaImageBean
import com.whg.eyepetizer.mvp.model.bean.RetrofitClient
import io.reactivex.Observable

/**
 * Created by guanhuawei on 2017/8/30.
 */
class KuulaIamgeModel() {
    fun loadData(context:Context,id: String): Observable<KuulaImageBean>? {
        val retrofitClient = RetrofitClient.getInstance(context, ApiService.KUULA_HOST)
        val apiService = retrofitClient.create(ApiService::class.java)
        return apiService?.getKuulaImage(id)
    }
}