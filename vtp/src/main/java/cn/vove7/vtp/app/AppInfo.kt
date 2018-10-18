package cn.vove7.vtp.app

import android.content.Context
import android.content.pm.PackageInfo
import android.graphics.drawable.Drawable

/**
 * App信息类
 */
open class AppInfo(
        var name: String = "",
        var alias: String = name,
        var packageName: String = "",
//        var icon: Drawable? = null,
        var versionName: String? = null,
        var versionCode: Int = 0,
        var packageInfo: PackageInfo,
        var pid: Int = 0,
        var priority: Int = 0
) {
    fun getIcon(context: Context): Drawable? = packageInfo.applicationInfo.loadIcon(context.packageManager)
    override fun toString(): String {
        return "AppInfo(name='$name', packageName='$packageName', versionName='$versionName', versionCode=$versionCode)"
    }
}