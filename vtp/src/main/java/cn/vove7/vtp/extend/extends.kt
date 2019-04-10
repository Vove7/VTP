package cn.vove7.vtp.extend

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.view.View
import cn.vove7.vtp.log.Vog
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*


/**
 * 扩展函数
 * @author vove
 * 2018/10/25
 */
/**
 * 代码块运行于UI线程
 * @param action () -> Unit
 */
fun runOnUi(action: () -> Unit) {
    val mainLoop: Looper
    try {
        mainLoop = Looper.getMainLooper()
    } catch (e: RuntimeException) {
        action.invoke()
        return
    }
    if (mainLoop == Looper.myLooper()) {
        action.invoke()
    } else {
        try {
            Handler(mainLoop).post(action)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun runOnNewHandlerThread(name: String = "anonymous", autoQuit: Boolean = true,
                          delay: Long = 0, run: () -> Unit): HandlerThread {
    return HandlerThread(name).apply {
        start()
        Vog.d(name)
        Handler(looper).postDelayed({
            run.invoke()
            if (autoQuit)
                quitSafely()
        }, delay)
    }
}

/**
 * 循环执行等待结果；超时返回空
 * eg用于视图搜索
 * @param waitMillis Long
 * @param run () -> T 返回空时，重新执行，直到超时
 * @return T
 */
fun <T> whileWaitTime(waitMillis: Long, run: () -> T?): T? {
    val begin = System.currentTimeMillis()
    var now: Long
    val ct = Thread.currentThread()
    do {
        run.invoke()?.also {
            //if 耗时操作
            return it
        }
        now = System.currentTimeMillis()
    } while (now - begin < waitMillis && !ct.isInterrupted)
    return null
}

/**
 * 循环执行等待run结果；超过次数返回空
 *
 * @param waitCount Int
 * @param run () -> T?
 * @return T? 执行结果
 */
fun <T> whileWaitCount(waitCount: Int, run: () -> T?): T? {
    var count = 0
    val ct = Thread.currentThread()
    while (count++ < waitCount && !ct.isInterrupted) {
        run.invoke()?.also {
            //if 耗时操作
            return it
        }
    }
    return null
}

fun prints(vararg msgs: Any?) {
    msgs.forEach {
        print(it)
        print(" ")
    }
}

fun formatNow(pat: String): String = SimpleDateFormat(pat, Locale.getDefault()).format(Date())

fun Context.startActivityOnNewTask(intent: Intent) {
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}

fun Context.startActivityOnNewTask(actCls: Class<*>) {
    val intent = Intent(this, actCls)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}


fun Intent.newTask(): Intent {
    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    return this
}



fun View.toggleVisibility(toggleVisibility: Int = View.GONE) {
    runOnUi {
        visibility = if (visibility == toggleVisibility) {
            View.VISIBLE
        } else {
            toggleVisibility
        }
    }
}

fun View.isVisibility(): Boolean = visibility == View.VISIBLE

fun View.disable() {
    runOnUi {
        isEnabled = false
    }
}

fun View.enable() {
    runOnUi {
        isEnabled = true
    }
}

fun View.gone() {
    runOnUi {
        visibility = View.GONE
    }
}

fun View.show() {
    runOnUi {
        visibility = View.VISIBLE
    }
}

operator fun String.times(number: Int): String {
    return buildString {
        for (i in 1..number) {
            append(this@times)
        }
    }
}


inline fun <reified T> buildList(builderAction: MutableList<T>.() -> Unit): List<T> =
    mutableListOf<T>().apply(builderAction)


/**
 * 使用四舍五入，取位数
 * @receiver Float
 * @param scale Int 精确位数
 * @return Float
 */
fun Float.scale(scale: Int): Float {
    //表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
    val roundingMode = RoundingMode.HALF_UP
    var bd = BigDecimal(this.toDouble())
    bd = bd.setScale(scale, roundingMode)
    return bd.toFloat()
}

fun Double.scale(scale: Int): Double {
    //表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
    val roundingMode = RoundingMode.HALF_UP
    var bd = BigDecimal(this)
    bd = bd.setScale(scale, roundingMode)
    return bd.toDouble()
}

/**
 * 当没有key时,设置map[k] = v 并返回v
 * @receiver Map<K, V>
 * @param k K
 * @param v V
 * @return V
 */
inline fun <reified K, reified V> HashMap<K, V>.getOrSetDefault(k: K, v: V): V {
    return if (containsKey(k)) {
        get(k)!!
    } else {
        this[k] = v
        v
    }
}
