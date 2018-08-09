package cn.vove7.vtp.activity

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.util.DisplayMetrics


/**
 * # ActivityHelper
 *
 * Created by Vove on 2018/7/29
 */
object ActivityHelper {

    /**
     * 截取当前Activity的屏幕内容
     */
    fun activityShot(activity: Activity): Bitmap {
        val view = activity.window.decorView
        view.setWillNotCacheDrawing(false)
        view.buildDrawingCache()
        val rect = Rect()

        view.getWindowVisibleDisplayFrame(rect)
        val statusbarHeight = rect.top
        val winManager = activity.windowManager

        val outMetrics = DisplayMetrics()
        winManager.defaultDisplay.getMetrics(outMetrics)

        val w = outMetrics.widthPixels
        val h = outMetrics.heightPixels

        //去除状态栏
        val bm = Bitmap.createBitmap(view.drawingCache, 0, statusbarHeight, w, h - statusbarHeight)
        view.setWillNotCacheDrawing(true)
        return bm
    }

    /**
     * 检测activity是否在栈顶
     */
    fun isForeground(context: Context, activity: Class<*>): Boolean {
        val myManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val task = myManager.getRunningTasks(1)
        val componentInfo = task[0].topActivity
        return componentInfo.packageName == activity::class.java.name
    }
}