package com.whg.eyepetizer.ui

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.shuyu.gsyvideoplayer.GSYVideoPlayer
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.whg.eyepetizer.R
import com.whg.eyepetizer.base.BaseActivity
import com.whg.eyepetizer.mvp.model.bean.VideoBean
import com.whg.eyepetizer.utils.*
import kotlinx.android.synthetic.main.activity_video_detail.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.find
import org.jetbrains.anko.toast
import zlc.season.rxdownload2.RxDownload
import java.io.FileInputStream

/**
 * Created by guanhuawei on 2017/8/29.
 */
class VideoDetailActivity : BaseActivity() {
    override val toolbar by lazy { find<Toolbar>(R.id.toolbar) }
    override val isFullScreen: Boolean = false


    var mContext: Context = this
    lateinit var imageView: ImageView
    lateinit var bean: VideoBean
    var isPlay: Boolean = false
    var isPause: Boolean = false
    lateinit var orientationUtils: OrientationUtils


    override fun getLayoutId(): Int {
        return R.layout.activity_video_detail
    }

    override fun initPresenter() {
    }

    override fun initView() {
        bean = intent.getParcelableExtra<VideoBean>("data")
        initViews()
        prepareVideo()
    }

    private fun initViews() {
        with(bean) {
            ImageLoadUtils.displayHigh(this@VideoDetailActivity, iv_bottom_bg, bean.blurred!!)
            tv_video_desc.text = description
            tv_video_desc.typeface = Typeface.createFromAsset(this@VideoDetailActivity.assets, "fonts/FZLanTingHeiS-DB1-GB-Regular.TTF")
            tv_video_title.text = title
            tv_video_title.typeface = Typeface.createFromAsset(this@VideoDetailActivity.assets, "fonts/FZLanTingHeiS-L-GB-Regular.TTF")
            var (date, realMinute: String, realSecond: String) = CVideoBeanUtils.triple(duration!!, time!!)
            tv_video_time.text = "$category / $realMinute'$realSecond''"
            tv_video_favor.text = collect.toString()
            tv_video_share.text = share.toString()
            tv_video_reply.text = share.toString()
        }

        tv_video_download.setOnClickListener {
            //点击下载
            var url = bean.playUrl?.let { it1 -> SPUtils.getInstance(this, "downloads").getString(it1) }
            if (url.equals("")) {
                var count = SPUtils.getInstance(this, "downloads").getInt("count")
                if (count != -1) {
                    count = count.inc()
                } else {
                    count = 1
                }
                SPUtils.getInstance(this, "downloads").put("count", count)
                ObjectSaveUtils.saveObject(this, "download$count", bean)
                addMission(bean.playUrl, count)
            } else {
                toast("该视频已经缓存过了")
            }
        }
    }

    private fun addMission(playUrl: String?, count: Int) {
        RxDownload.getInstance(this).serviceDownload(playUrl, "download$count").subscribe({
            toast("开始下载")
            SPUtils.getInstance(this, "downloads").put(bean.playUrl.toString(), bean.playUrl.toString())
            SPUtils.getInstance(this, "download_state").put(playUrl.toString(), true)
        }, {
            toast("添加任务失败")
        })
    }

    private fun prepareVideo() {
        var uri = intent.getStringExtra("loaclFile")

        //增加封面
        imageView = ImageView(this)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP

        // ImageViewAsyncTask(mHandler, this, imageView).execute(bean.feed)
        setImagview()

        with(gsy_player) {
            if (uri != null) {
                Log.e("uri", uri)
                setUp(uri, false, null, null)
            } else {
                setUp(bean.playUrl, false, null, null)
            }
            titleTextView.visibility = View.GONE
            backButton.visibility = View.VISIBLE
            orientationUtils = OrientationUtils(this@VideoDetailActivity, this)
            setIsTouchWiget(true)
            //关闭自动旋转
            isRotateViewAuto = false
            isLockLand = false
            isShowFullAnimation = false
            isNeedLockFull = true
            fullscreenButton.setOnClickListener {
                //直接横屏
                orientationUtils.resolveByClick()
                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                startWindowFullscreen(mContext, true, true)
            }
            setStandardVideoAllCallBack(object : VideoListener() {
                override fun onPrepared(url: String?, vararg objects: Any?) {
                    super.onPrepared(url, *objects)
                    //开始播放了才能旋转和全屏
                    orientationUtils.isEnable = true
                    isPlay = true
                }

                override fun onAutoComplete(url: String?, vararg objects: Any?) {
                    super.onAutoComplete(url, *objects)

                }

                override fun onClickStartError(url: String?, vararg objects: Any?) {
                    super.onClickStartError(url, *objects)
                }

                override fun onQuitFullscreen(url: String?, vararg objects: Any?) {
                    super.onQuitFullscreen(url, *objects)
                    orientationUtils.let { orientationUtils.backToProtVideo() }
                }
            })
            setLockClickListener { view, lock ->
                //配合下方的onConfigurationChanged
                orientationUtils.isEnable = !lock
            }
            backButton.setOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun setImagview() = async(UI) {

        val result = bg {
            getBitmap()
        }
        updateUI(result.await())
    }

    private fun getBitmap(): Bitmap {
        val future = Glide.with(this@VideoDetailActivity)
                .load(bean.feed)
                .downloadOnly(100, 100)
        return BitmapFactory.decodeStream(FileInputStream(future?.get()?.absolutePath))
    }

    private fun updateUI(bitmap: Bitmap?) {
        imageView.setImageBitmap(bitmap!!)
        gsy_player.setThumbImageView(imageView)
    }


    override fun onBackPressed() {
        orientationUtils?.let {
            orientationUtils?.backToProtVideo()
        }
        if (StandardGSYVideoPlayer.backFromWindowFull(this)) {
            return
        }
        super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        isPause = true
    }

    override fun onResume() {
        super.onResume()
        isPause = false
    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoPlayer.releaseAllVideos()
        orientationUtils?.let {
            orientationUtils?.releaseListener()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        if (isPlay && !isPause) {
            if (newConfig?.orientation == ActivityInfo.SCREEN_ORIENTATION_USER) {
                if (!gsy_player.isIfCurrentIsFullscreen) {
                    gsy_player.startWindowFullscreen(mContext, true, true)
                }
            } else {
                //新版本isIfCurrentIsFullscreen的标志位内部提前设置了，所以不会和手动点击冲突
                if (gsy_player.isIfCurrentIsFullscreen) {
                    StandardGSYVideoPlayer.backFromWindowFull(this)
                }
                orientationUtils.let { orientationUtils.isEnable = true }
            }
        }
    }
}