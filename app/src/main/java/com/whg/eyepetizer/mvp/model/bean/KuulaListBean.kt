package com.whg.eyepetizer.mvp.model.bean

/**
 * Created by guanhuawei on 2017/8/30.
 */
data class KuulaListBean(var status: Int, var payload: PayloadBean, var action: String, var exectime: Int) {


    data class PayloadBean(var page: PageBean, var posts: List<PostsBean>) {

        data class PageBean(var total: Int, var size: Int, var index: Int, var start: Int,
                            var isHasPrev: Boolean, var isHasNext: Boolean, var nextIndex: Int)

        data class PostsBean(var id: String, var uuid: String, var description: String, var cover: String,
                             var privacy: String, var views: String, var tiny: String, var featured: String,
                             var created: String, var comments: String, var likes: String, var user: UserBean) {
            data class UserBean(var id: String, var name: String, var displayname: Any, var picture: String, var type: String)
        }
    }
}