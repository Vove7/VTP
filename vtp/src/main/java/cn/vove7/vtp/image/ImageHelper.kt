package cn.vove7.vtp.image

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import jp.wasabeef.glide.transformations.BlurTransformation

/**
 * # ImageHelper
 *
 * Created by Vove on 2018/7/29
 */
object ImageHelper {
    /**
     * 给View设置高斯模糊背景
     */
    fun setGsImage(context: Context, view: View, d: Drawable, radius: Int = 45, sampling: Int = 3) {

        val simpleTarget = object : SimpleTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                view.background = resource
            }
        }
        Glide.with(context).load(d)
                .apply(RequestOptions.bitmapTransform(BlurTransformation(radius, sampling)))
                .into(simpleTarget)
    }

}