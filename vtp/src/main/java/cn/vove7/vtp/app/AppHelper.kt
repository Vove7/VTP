@file:Suppress("unused")

package cn.vove7.vtp.app

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings


/**
 *
 *
 * Created by Vove on 2018/6/14
 */
object AppHelper {

    /**
     * 跳转App详情页
     */
    fun showPackageDetail(context: Context, packageName: String) {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }

    /**
     * 根据App name 或者 pkg获取AppInfo
     */
    fun getAppInfo(context: Context, name: String, pkg: String): AppInfo? {
        val man = context.packageManager
        val list = man.getInstalledPackages(0)
        for (app in list) {
            val appName = app.applicationInfo.loadLabel(man).toString()

            if (name == appName || pkg == app.packageName) {
                return AppInfo(packageName = app.packageName)
            }
        }
        return null
    }

    /**
     * 获取所有已安装
     * @param includeSelf 是否包含自己
     */
    fun getAllInstallApp(context: Context, includeSelf: Boolean = true): List<AppInfo> {
        val man = context.packageManager
        val list = man.getInstalledPackages(0)
        val appList = mutableListOf<AppInfo>()
        for (app in list) {
            val name = app.applicationInfo.loadLabel(man).toString()
            if (app.packageName == context.packageName && includeSelf) {
                continue
            }
            try {
                appList.add(AppInfo(app.packageName))
            } catch (e: Exception) {//NameNotFoundException
                e.printStackTrace()
            }
        }
        return appList
    }

    /**
     * 获取所有可启动的App信息
     * @param includeSelf 是否包含自己
     */
    fun getAllLaunchableApp(context: Context, includeSelf: Boolean = true): List<AppInfo> {
        val appList = mutableListOf<AppInfo>()

        val pm = context.packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val mResolveInfo = pm.queryIntentActivities(mainIntent, 0)
        for (info in mResolveInfo) {
            val packName = info.activityInfo.packageName
            if (packName == context.packageName && includeSelf) {
                continue
            }
            val pkgInfo = pm.getPackageInfo(packName, 0)
            val mInfo = AppInfo(packageName = packName)
            appList.add(mInfo)
        }
        return appList
    }

    /**
     * 返回桌面
     */
    fun toHome(context: Context) {
        val home = Intent(Intent.ACTION_MAIN)
        home.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        home.addCategory(Intent.CATEGORY_HOME)
        context.startActivity(home)
    }
}