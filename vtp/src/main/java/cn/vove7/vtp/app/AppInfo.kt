package cn.vove7.vtp.app

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable

/**
 * App信息类
 */
open class AppInfo(val packageName: String) {
    val name: String?
        get() = packageInfo?.applicationInfo?.loadLabel(app.packageManager)?.toString()
    val icon: Drawable?
        get() = packageInfo?.applicationInfo?.loadIcon(app.packageManager)

    override fun toString(): String {
        return "AppInfo(name='$name', packageName='$packageName', versionName='$versionName', versionCode=$versionCode)"
    }

    private val packageInfo: PackageInfo?
        get() = app.packageManager.getPackageInfo(packageName, 0)
    val versionName: String? get() = packageInfo?.versionName

    var versionCode: Int = packageInfo?.versionCode ?: -1
    var priority: Int = 0

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var app: Context

        /**
         * 在Application中初始化
         * @param app Context
         */
        fun attachApplication(app: Context) {
            this.app = app
        }

        /**
         * 获取桌面应用
         * @param context Context
         * @return List<String>
         */
        fun getHomes(context: Context): List<String> {
            val homeAppPkgs = arrayListOf<String>()
            val packageManager = context.packageManager
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            val resolveInfo = packageManager.queryIntentActivities(intent,
                    PackageManager.MATCH_DEFAULT_ONLY)
            resolveInfo?.forEach {
                homeAppPkgs.add(it.activityInfo.packageName)
            }
            return homeAppPkgs
        }
    }

    /**
     * 是否为用户安装应用
     */
    val isUserApp
        get() = ((packageInfo?.applicationInfo?.flags ?: 0) and ApplicationInfo.FLAG_SYSTEM) == 0

    /**
     * 是否为系统应用
     */
    val isSysApp
        get() = ((packageInfo?.applicationInfo?.flags ?: 0) and ApplicationInfo.FLAG_SYSTEM) == 0

    /**
     * 是否授权此权限
     * @param p String
     * @return Boolean
     */
    fun hasGrantedPermission(p: String): Boolean {
        val pm = app.packageManager

        return (PackageManager.PERMISSION_GRANTED == pm.checkPermission(p, packageName))
    }

    /**
     * 是否为输入法
     */
    val isInputMethod: Boolean
        get() {
            val pm = app.packageManager
            val pkgInfo = pm.getPackageInfo(packageName, PackageManager.GET_SERVICES)
            pkgInfo.services?.forEach {
                if (it.permission == Manifest.permission.BIND_INPUT_METHOD) {
                    return true
                }
            }
            return false
        }

    /**
     * 获取活动列表
     */
    val activities: Array<String>
        get() {
            synchronized(AppInfo::class) {
                val pm = app.packageManager
                val pkgInfo = pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
                val acs = pkgInfo.activities
                val rList = mutableListOf<String>()
                acs?.forEach { ac ->
                    ac.name?.also {
                        rList.add(it)
                    }
                }
                return rList.toTypedArray()
            }
        }
}