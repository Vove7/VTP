package cn.vove7.vtp.toast

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.annotation.DrawableRes
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import cn.vove7.vtp.R
import cn.vove7.vtp.log.Vog

/**
 * # Voast like a Toast which with a oval rect white background
 * Usage :
 * lateinit var toast: Voast
 * toast = Voast.with(context).top()
 * toast.showShort("message")
 */
class Voast(
        private val context: Context,
        private val outDebug: Boolean
) {
    private lateinit var msgView: TextView
    private lateinit var icon: ImageView
    private val animations = -1
    private lateinit var toast: Toast
    @SuppressLint("ShowToast")

    fun build(): Voast {
        toast = Toast.makeText(context, "", Toast.LENGTH_SHORT)!!
        val inflate = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflate.inflate(R.layout.vtoast_layout, null)
        msgView = v.findViewById(R.id.message) as TextView
        icon = v.findViewById(R.id.icon) as ImageView
        toast.view = v

        return bottom()
    }

    private fun setIconWithDrawableId(dId: Int) {
        if (dId != -1) {
            icon.visibility = View.VISIBLE
            icon.setImageResource(dId)
        } else {
            icon.visibility = View.GONE
        }
    }

    private fun setIconWithDrawable(d: Drawable?) {
        if (d != null) {
            icon.visibility = View.VISIBLE
            icon.setImageDrawable(d)
        } else {
            icon.visibility = View.GONE
        }
    }


    fun top(xOffset: Int = 0, yOffset: Int = 40): Voast {
        toast.setGravity(Gravity.TOP, xOffset, yOffset)
        return this
    }

    fun bottom(xOffset: Int = 0, yOffset: Int = 0): Voast {
        toast.setGravity(Gravity.BOTTOM, xOffset, yOffset)
        return this
    }

    fun center(xOffset: Int = 0, yOffset: Int = 0): Voast {
        toast.setGravity(Gravity.CENTER, xOffset, yOffset)
        return this
    }

    fun showShort(msg: String, @DrawableRes dId: Int = -1) {
        setIconWithDrawableId(dId)
        show(msg, Toast.LENGTH_SHORT)
    }

    fun showShort(msg: String) {
        setIconWithDrawable(null)
        show(msg, Toast.LENGTH_SHORT)
    }

    fun showShort(msg: String, d: Drawable? = null) {
        setIconWithDrawable(d)
        show(msg, Toast.LENGTH_SHORT)
    }

    fun showLong(msg: String, @DrawableRes dId: Int = -1) {
        setIconWithDrawableId(dId)
        show(msg, Toast.LENGTH_LONG)
    }

    fun showLong(msg: String, d: Drawable? = null) {
        setIconWithDrawable(d)
        show(msg, Toast.LENGTH_LONG)
    }

    fun showLong(msg: String) {
        setIconWithDrawable(null)
        show(msg, Toast.LENGTH_LONG)
    }

    private val voastHandler = VoastHandler(this)

    fun show(msg: String, d: Int = Toast.LENGTH_SHORT) {
        if (outDebug)
            Vog.d(this, msg)

        voastHandler.sendMessage(voastHandler.buildVoastMessage(msg, d))
    }

    companion object {
        fun with(context: Context, outDebug: Boolean = false): Voast {
            return Voast(context, outDebug).build()
        }
    }

    class VoastHandler(private val voast:Voast) : Handler(Looper.getMainLooper()) {

        override fun handleMessage(msg: Message?) {
            if (msg == null) return
            val m = msg.data["msg"] as String?
            val l = msg.data["l"] as Int
            try {
                voast.toast.duration = l
                voast.msgView.text = m
            } catch (e: Exception) {
                e.printStackTrace()
            }
            voast.toast.show()
        }

        fun buildVoastMessage(msg: String, l: Int = Toast.LENGTH_SHORT): Message {
            val data = Bundle()
            data.putString("msg", msg)
            data.putInt("l", l)
            val m = Message()
            m.data = data
            return m
        }
    }
}