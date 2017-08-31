package com.whg.eyepetizer.ui

import android.graphics.Typeface
import android.support.v7.widget.Toolbar
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import com.whg.eyepetizer.R
import com.whg.eyepetizer.base.BaseActivity
import com.whg.eyepetizer.utils.newIntent
import kotlinx.android.synthetic.main.activity_splash.*
import org.jetbrains.anko.find

/**
 * Created by guanhuawei on 2017/8/28.
 */
class SplashActivity : BaseActivity() {
    override val toolbar by lazy { find<Toolbar>(R.id.toolbar) }
    override val isFullScreen: Boolean = true


    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun initPresenter() {

    }



    override fun initView() {
        setFullScreen()
        val font: Typeface = Typeface.createFromAsset(this.assets, "fonts/Lobster-1.4.otf")
        tv_name_english.typeface = font
        tv_english_intro.typeface = font

        setAnimation()
    }

    private fun setAnimation() {
        val alphaAnimation = AlphaAnimation(0.1f, 0.1f)
        alphaAnimation.duration = 1000
        val scaleAnimation = ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f)
        scaleAnimation.duration = 1000
        val animationSet = AnimationSet(true)
        animationSet.addAnimation(alphaAnimation)
        animationSet.addAnimation(scaleAnimation)
        animationSet.duration = 1000
        iv_icon_splash.startAnimation(animationSet)
        animationSet.setAnimationListener(object:Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                newIntent<MainActivity>()
                finish()
            }

            override fun onAnimationStart(animation: Animation?) {
            }
        })


    }
}