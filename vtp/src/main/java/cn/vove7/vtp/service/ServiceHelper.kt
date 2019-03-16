package cn.vove7.vtp.service

import android.app.ActivityManager
import android.content.Context
import cn.vove7.vtp.log.Vog

/**
 * # ServiceHelper
 *
 * @author Vove
 * 2018/8/9
 */
object ServiceHelper {
    /**
     * 判断服务是否开启
     *
     * @return
     */
    fun isServiceRunning(context: Context, service: Class<*>): Boolean {
        val sName = service::class.java.name
        val manager = context
                .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningService = manager.getRunningServices(Int.MAX_VALUE)
        runningService.forEach {
           Vog.d( "$it - $sName")
            if (it.service.className == sName) {
                return true
            }
        }
        return false
    }
}