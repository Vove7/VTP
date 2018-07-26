package cn.vove7.vtp.view.span

import android.content.Context
import android.support.annotation.ColorRes
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

import cn.vove7.vtp.R
import cn.vove7.vtp.view.DisplayUtils

/**
 * # ColourTextClickableSpan
 * 指定颜色可点击文本
 *
 *
 * Create By Vove
 */
class ColourTextClickableSpan
/**
 * @param fontSize 单位sp
 */
(internal var context: Context, text: String,
 @ColorRes private val colorId: Int = defaultColor, private var fontSize: Int = -1,
 private val listener: View.OnClickListener?) : ClickableSpan() {
    var spanStr: SpannableString

    init {
        if (fontSize > 0)
            this.fontSize = DisplayUtils.sp2px(context, fontSize.toFloat())
        spanStr = SpannableString(text)
        spanStr.setSpan(this, 0, text.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
    }

    override fun onClick(widget: View) {
        listener?.onClick(widget)
    }

    override fun updateDrawState(ds: TextPaint) {
        ds.color = context.resources.getColor(colorId)
        if (fontSize > 0)
            ds.textSize = fontSize.toFloat()
    }

    companion object {

        private val defaultColor = R.color.default_text_color
    }
}