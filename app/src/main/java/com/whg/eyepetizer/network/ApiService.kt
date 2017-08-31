package com.whg.eyepetizer.mvp.model.bean

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Created by guanhuawei on 2017/8/30.
 */
interface ApiService {
    companion object {
        val BASE_URL: String = "http://baobab.kaiyanapp.com/api/"

        val KUULA_HOST: String = "https://kuula.co/api/"
    }


    //获取首页第一页数据
    @GET("v2/feed?num=2&udid=26868b32e808498db32fd51fb422d00175e179df&vc=83")
    fun getHomeData(): Observable<HomeBean>


    //获取首页第一页之后的数据  ?date=1499043600000&num=2
    @GET("v2/feed")
    fun getHomeMoreData(@Query("date") date: String, @Query("num") num: String): Observable<HomeBean>

    //获取发现频道信息
    @GET("v2/categories?udid=26868b32e808498db32fd51fb422d00175e179df&vc=83")
    fun getFindData(): Observable<ArrayList<FindBean>>


    //获取热门排行信息
    @GET("v3/ranklist")
    fun getHotData(@Query("num") num: Int, @Query("strategy") strategy: String,
                   @Query("udid") udid: String, @Query("vc") vc: Int): Observable<HotBean>

    //获取发现频道详情信息
    @GET("v3/videos")
    fun getFindDetailData(@Query("categoryName") categoryName: String, @Query("strategy") strategy: String,
                          @Query("udid") udid: String, @Query("vc") vc: Int): Observable<HotBean>

    //获取发现详情加载更多消息
    @GET("v3/videos")
    fun getFindDetailMoreData(@Query("start") start: Int, @Query("num") num: Int,
                              @Query("categoryName") categoryName: String, @Query("strategy") strategy: String): Observable<HotBean>

    //获取关键词搜索相关信息
    @GET("v1/search")
    fun getSearchData(@Query("num") num: Int, @Query("query") query: String,
                      @Query("start") start: Int): Observable<HotBean>


    @GET("?action=explore&tag=featured&limit=12&time=0&app=vr")
    fun getKuulaList(@Query("offset") offset: Int): Observable<KuulaListBean>

    @GET("?action=get&app=vr")
    fun getKuulaImage(@Query("id") id: String): Observable<KuulaImageBean>

}