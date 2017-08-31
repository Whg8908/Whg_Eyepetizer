package com.whg.eyepetizer.mvp.model.bean

/**
 * Created by guanhuawei on 2017/8/30.
 */
data class KuulaImageBean(var status: Int, var payload: PayloadBean, var action: String, var exectime: Int) {

    data class PayloadBean(var id: String, var uuid: String, var description: String, var cover: String, var views: Int,
                           var privacy: String,
                           var featured: String,
                           var tiny: String,
                           var created: String,
                           var user: UserBean,
                           var n: String,
                           var likes: Int,
                           var comments: Int,
                           var photos: List<PhotosBean>,
                           var wholiked: List<WholikedBean>?

    )


    data class UserBean(var id: String, var name: String, var type: String, var displayname: String, var picture: String, var website: String, var settings: Any, var url: String)

    data class PhotosBean(var id: String, var name: String, var options: OptionsBean, var sizes: List<String>, var urls: List<String>) {

        data class OptionsBean(var heading: Double = 0.toDouble(),
                               var roll: Double = 0.toDouble(),
                               var pitch: Double = 0.toDouble(),
                               var zoom: Double = 0.toDouble(),
                               var isLensflare: Boolean = false,
                               var glare: Double = 0.toDouble(),
                               var sunIntensity: Double = 0.toDouble(),
                               var tinyRot: Double = 0.toDouble(),
                               var tinyScale: Double = 0.toDouble(),
                               var tinyBulge: Double = 0.toDouble(),
                               var tinyInvert: Double = 0.toDouble(),
                               var tinyOffsetX: Double = 0.toDouble(),
                               var tinyOffsetY: Double = 0.toDouble(),
                               var isTinySaved: Boolean = false,
                               var sun: Any? = null,
                               var filter: String? = null,
                               var filterintensity: Double = 0.toDouble(),
                               var format: String? = null)
    }

    data class WholikedBean(var name: String, var displayname: String)

}
