package cn.vove7.vtp.view.popup

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import android.support.annotation.LayoutRes
import android.view.*
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import cn.vove7.vtp.R
import cn.vove7.vtp.activity.ActivityHelper
import cn.vove7.vtp.log.Vog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import jp.wasabeef.glide.transformations.BlurTransformation

/**
 * 创建App内悬浮窗
 */
object PopupHelper {
    /**
     * Create a PopupWindow with a layoutRes
     */
    fun createPop(context: Context, @LayoutRes resId: Int, w: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
                  h: Int = ViewGroup.LayoutParams.WRAP_CONTENT): PopupWindow {
        val contentView = LayoutInflater.from(context).inflate(resId, null)
        return createPop(contentView, w, h)
    }

    /**
     * Create a PopupWindow with a View
     */
    fun createPop(contentView: View, w: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
                  h: Int = ViewGroup.LayoutParams.WRAP_CONTENT): PopupWindow {
        val popW = PopupWindow(contentView, w, h, false)
        popW.contentView = contentView
        return popW
    }

    /**
     * Create a PopupWindow
     * which like @Toast with @CardView
     */
    fun createPopCard(context: Context, content: String): PopupWindow {
        val contentView = LayoutInflater.from(context).inflate(R.layout.pop_card, null)
        contentView.findViewById<TextView>(R.id.message).text = content
        return createPop(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    /**
     * gasBlur : 背景使用高斯模糊
     * background 背景 Activity==null时启用
     */
    fun createPreViewImageAndShow(activity: Activity, parent: View, previewDrawable: Drawable,
                                  gasBlur: Boolean = true, background: Drawable? = null,
                                  cirReveal: Boolean = true): PopupWindow {

        val contentView = LayoutInflater.from(activity as Context).inflate(R.layout.pop_preview_image, null)

        val previewHolder = contentView.findViewById<ImageView>(R.id.preview_holder)

        if (gasBlur) {
            val simpleTarget = object : SimpleTarget<Drawable>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    contentView.background = resource
                }
            }
            Glide.with(activity).load(ActivityHelper.activityShot(activity))
                    .apply(bitmapTransform(BlurTransformation(45, 3)))
                    .into(simpleTarget)
        } else if (background != null) {
            contentView.background = background
        }

        previewHolder.setImageDrawable(previewDrawable)
        val p = createPop(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        p.animationStyle = R.style.pop_anim
        p.showAtLocation(parent, Gravity.CENTER, 0, 0)

        if (cirReveal) {
            val centerX = (contentView.left + contentView.right) / 2
            val centerY = (contentView.top + contentView.bottom) / 2
            val finalRadius = contentView.height / 2
            // 定义揭露动画

            val mCircularReveal = ViewAnimationUtils.createCircularReveal(
                    contentView, centerX, centerY, 0f, finalRadius.toFloat())
            // 设置动画持续时间，并开始动画
            mCircularReveal.duration = 500
            mCircularReveal.start()
        }

        return p
    }

    /**
     * 提示框
     */
    fun createTooltipAndShow(context: Context, msg: String, target: View, delayMillis: Long = -1): PopupWindow {
        val contentView = LayoutInflater.from(context).inflate(R.layout.pop_tooltip, null)
        contentView.findViewById<TextView>(R.id.message).text = msg
        val p = createPop(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        p.animationStyle = R.style.pop_anim
        contentView.measure(WRAP_CONTENT, WRAP_CONTENT)

        val x = ((target.left + target.right) / 2) - (contentView.width / 2)
        val y = target.top - contentView.height
       Vog.d( "$x $y")
        p.showAtLocation(target,Gravity.CENTER,0,0)
        if (delayMillis > 0) {
            delayHide(p, delayMillis)
        }
        return p
    }

    fun delayHide(p: PopupWindow, delayMillis: Long) {
        Handler().postDelayed({
            p.dismiss()
        }, delayMillis)
    }
}
