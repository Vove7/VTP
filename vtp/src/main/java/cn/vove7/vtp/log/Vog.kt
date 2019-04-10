package cn.vove7.vtp.log

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("StaticFieldLeak")
/**
 * #Vog
 *
 */
object Vog {
    private var output_level = Log.VERBOSE

    /**
     * @param outputLevel Log.***
     */
    fun init(context: Context, outputLevel: Int): Vog {
        output_level = outputLevel
        return this
    }

    fun d(msg: Any?) {
        println(Log.DEBUG, msg.toString())
    }

    fun wtf(msg: Any?) {
        println(Log.ERROR, msg.toString())
    }

    fun v(msg: Any) {
        println(Log.VERBOSE, msg.toString())
    }

    fun i(msg: Any) {
        println(Log.INFO, msg.toString())
    }

    fun w(msg: Any) {
        println(Log.WARN, msg.toString())
    }

    fun e(msg: Any) {
        println(Log.ERROR, msg.toString())
    }

    fun a(msg: Any) {
        println(Log.ASSERT, msg.toString())
    }

    private val dateFormat: DateFormat
        get() =
            SimpleDateFormat("MM-dd hh-mm-ss", Locale.CHINA)

    const val TAG = "VOG"

    private fun println(priority: Int, msg: String) {
        if (output_level > priority)
            return//省运算

        val pre = findCaller(3)?.let {
            (it.methodName + "(" + it.fileName +
                    ":" + it.lineNumber + ")")
        }
        try {
            Log.println(priority, TAG, "$pre  >> $msg\n")
        } catch (e: Exception) {
            when (priority) {
                Log.ERROR -> System.err.println("$pre  >> $msg")
                else -> println("$pre  >> $msg")
            }
        }
    }

    private fun findCaller(upDepth: Int): StackTraceElement? {
        // 获取堆栈信息
        val callStack = Thread.currentThread().stackTrace
        // 最原始被调用的堆栈信息
        // 日志类名称
        val logClassName = Vog::class.java.name
        // 循环遍历到日志类标识
        var i = 0
        val len = callStack.size
        while (i < len) {
            if (logClassName == callStack[i].className)
                break
            i++
        }
        return try {
            callStack[i + upDepth]
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}
