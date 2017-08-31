package com.whg.eyepetizer.ui

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.opengl.GLSurfaceView
import android.support.v7.widget.Toolbar
import android.view.View
import com.asha.vrlib.MDDirectorCamUpdate
import com.asha.vrlib.MDVRLibrary
import com.asha.vrlib.texture.MD360BitmapTexture
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.whg.eyepetizer.R
import com.whg.eyepetizer.base.BaseActivity
import com.whg.eyepetizer.mvp.contract.KuulaImageContract
import com.whg.eyepetizer.mvp.model.bean.KuulaImageBean
import com.whg.eyepetizer.mvp.presenter.KuulaImagePresenter
import kotlinx.android.synthetic.main.veer_detail_fragment.*
import org.jetbrains.anko.find

/**
 * Created by guanhuawei on 2017/8/30.
 */
class VrActivity : BaseActivity(), KuulaImageContract.View {


    override val toolbar by lazy { find<Toolbar>(R.id.toolbar) }

    override val isFullScreen = true

    lateinit var mVRLibrary: MDVRLibrary

    lateinit var myGLSurfaceView: GLSurfaceView
    lateinit var id: String


    override fun getLayoutId() = R.layout.veer_detail_fragment

    lateinit var mPresenter: KuulaImagePresenter

    override fun initPresenter() {
        mPresenter = KuulaImagePresenter(this, this)
    }

    override fun initView() {
        setFullScreen()
        id = intent.getStringExtra("id")

        loading.start()
        myGLSurfaceView = GLSurfaceView(this)
        mVRLibrary = MDVRLibrary.with(this)
                .displayMode(MDVRLibrary.DISPLAY_MODE_NORMAL)
                .interactiveMode(MDVRLibrary.INTERACTIVE_MODE_MOTION_WITH_TOUCH)
                .asBitmap { callback ->
                    this.callback = callback
                }
                .listenTouchPick { hitHotspot, ray -> }
                .pinchEnabled(true)
                .build(myGLSurfaceView)
        glSurfaceView.addView(myGLSurfaceView)

        getData(id)

        mode.setOnClickListener {
            if (isNormal) {
                normalMode()
                mode.text = "鱼眼"
            } else {
                fishEyesMode()
                mode.text = "普通"
            }
        }
        sensor.setOnClickListener {
            if (!sensorOpen) {
                sensorOpen = true
                mVRLibrary.switchInteractiveMode(this, MDVRLibrary.INTERACTIVE_MODE_MOTION_WITH_TOUCH)
//                sensor.setImageResource(R.drawable.sensor_icon)
            } else {
                sensorOpen = false
                mVRLibrary.switchInteractiveMode(this, MDVRLibrary.INTERACTIVE_MODE_TOUCH)
//                sensor.setImageResource(R.drawable.unsensor_icon)
            }
        }
    }


    override fun showContent(kuulaImageBean: KuulaImageBean) {
        with(kuulaImageBean) {
            user_name.text = payload?.user?.name.toString()
            description.text = payload?.description.toString()
            val photos = payload!!.photos!![0]!!.urls!![1]
            loadImage(photos, callback)
        }
    }


    lateinit var callback: MD360BitmapTexture.Callback

    private fun loadImage(photos: String, callback: MD360BitmapTexture.Callback) {
        Glide.with(this)
                .load(photos)
                .asBitmap()
                .override(2048, 1096)
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(bitmap: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
                        getVRLibrary().onTextureResize(bitmap.getWidth().toFloat(), bitmap.getHeight().toFloat())
                        callback.texture(bitmap)
                        loading.stop()
                        loading.visibility = View.GONE
                    }
                })
    }

    fun getVRLibrary(): MDVRLibrary {
        return mVRLibrary
    }

    fun getData(id: String) {
        mPresenter.getVrCategoryData(id)
    }

    fun fishEyesMode() {
        isNormal = true
        val cameraUpdate = getVRLibrary().updateCamera()
        val near = PropertyValuesHolder.ofFloat("near", cameraUpdate.nearScale, -0.6f)
        val eyeZ = PropertyValuesHolder.ofFloat("eyeZ", cameraUpdate.eyeZ, 18f)
        val pitch = PropertyValuesHolder.ofFloat("pitch", cameraUpdate.pitch, 45f)
        val yaw = PropertyValuesHolder.ofFloat("yaw", cameraUpdate.yaw, 45f)
        val roll = PropertyValuesHolder.ofFloat("roll", cameraUpdate.roll, 0f)
        startCameraAnimation(cameraUpdate, near, eyeZ, pitch, yaw, roll)
    }

    fun normalMode() {
        isNormal = false
        val cameraUpdate = getVRLibrary().updateCamera()
        val near = PropertyValuesHolder.ofFloat("near", cameraUpdate.nearScale, 0f)
        val eyeZ = PropertyValuesHolder.ofFloat("eyeZ", cameraUpdate.eyeZ, 0f)
        val pitch = PropertyValuesHolder.ofFloat("pitch", cameraUpdate.pitch, 0f)
        val yaw = PropertyValuesHolder.ofFloat("yaw", cameraUpdate.yaw, 0f)
        val roll = PropertyValuesHolder.ofFloat("roll", cameraUpdate.roll, 0f)
        startCameraAnimation(cameraUpdate, near, eyeZ, pitch, yaw, roll)
    }


    private var isNormal: Boolean = false
    private var sensorOpen: Boolean = false
    private var animator: ValueAnimator? = null
    private fun startCameraAnimation(cameraUpdate: MDDirectorCamUpdate, vararg values: PropertyValuesHolder) {
        if (animator != null) {
            animator!!.cancel()
        }

        animator = ValueAnimator.ofPropertyValuesHolder(*values).setDuration(2000)
        animator!!.addUpdateListener { animation ->
            val near = animation.getAnimatedValue("near") as Float
            val eyeZ = animation.getAnimatedValue("eyeZ") as Float
            val pitch = animation.getAnimatedValue("pitch") as Float
            val yaw = animation.getAnimatedValue("yaw") as Float
            val roll = animation.getAnimatedValue("roll") as Float
            cameraUpdate.setEyeZ(eyeZ).setNearScale(near).setPitch(pitch).setYaw(yaw).roll = roll
        }
        animator!!.start()
    }


    override fun onResume() {
        super.onResume()
        mVRLibrary.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        mVRLibrary.onPause(this)
    }


    override fun onDestroy() {
        super.onDestroy()
        mVRLibrary.onDestroy()
    }
}