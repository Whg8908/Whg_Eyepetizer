package com.whg.eyepetizer.mvp.model

import android.content.Context
import com.whg.eyepetizer.mvp.model.bean.ApiService
import com.whg.eyepetizer.mvp.model.bean.KuulaListBean
import com.whg.eyepetizer.mvp.model.bean.RetrofitClient
import io.reactivex.Observable

/**
 * Created by guanhuawei on 2017/8/30.
 */
class VeerModel {

    fun getVrCategoryData(context: Context, offset: Int): Observable<KuulaListBean>? {
        val retrofitClient = RetrofitClient.getInstance(context, ApiService.KUULA_HOST)
        val apiService = retrofitClient.create(ApiService::class.java)
        return apiService?.getKuulaList(offset)
    }

}