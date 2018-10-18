package cn.vove7.vtp.system

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.telephony.TelephonyManager

/**
 *
 *
 * Created by Vove on 2018/6/24
 */
object SystemHelper {
    /**
     * 获取剪切板最新内容
     */
    fun getClipBoardContent(context: Context): CharSequence {
        val data = getClipData(context)
        return data.getItemAt(data.itemCount - 1).text
    }

    /**
     * 获取剪切板数据
     * @return ClipData
     */
    fun getClipData(context: Context): ClipData {
        val cs = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        return cs.primaryClip
    }

    fun saveToClipBoard(context: Context, text: String) {
        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val mClipData = ClipData.newPlainText("", text)
        cm.primaryClip = mClipData
    }

    /**
     * @return 亮屏 true else false
     */
    fun isScreenOn(context: Context): Boolean {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            powerManager.isInteractive
        } else {
            powerManager.isScreenOn
        }
    }

    /**
     * 打开链接
     */
    fun openLink(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        context.startActivity(intent)
    }


    const val APP_STORE_GOOGLE_PLAY = 0
    const val APP_STORE_COLL_APK = 1
    const val APP_STORE_WANDOUJIA = 2
    /**
     * 打开应用商店
     * @param appStoreInt [APP_STORE_GOOGLE_PLAY] [APP_STORE_COLL_APK] [APP_STORE_WANDOUJIA]
     */
    fun openApplicationMarket(context: Context, packageName: String, appStoreInt: Int) {
        try {
            val str = "market://details?id=$packageName"
            val localIntent = Intent(Intent.ACTION_VIEW)
            localIntent.data = Uri.parse(str)
            context.startActivity(localIntent)
        } catch (e: Exception) {
            // 打开应用商店失败 可能是没有手机没有安装应用市场
            e.printStackTrace()
            // 调用系统浏览器进入商城
            val url = APP_STORE_URLS[appStoreInt] + packageName
            openLink(context, url)
        }
    }

    private const val URL_COOL_APK = "https://www.coolapk.com/apk/"
    private const val URL_WANDOUJIA = "http://m.wandoujia.com/apps/"
    private const val URL_GOOGLE_PLAY_STORE = "https://play.google.com/store/apps/details?id="
    private val APP_STORE_URLS = arrayOf(
            URL_GOOGLE_PLAY_STORE
            , URL_COOL_APK
            , URL_WANDOUJIA
    )


    /**
     * 获取设备信息
     * @return [DeviceInfo]
     */
    fun getDeviceInfo(context: Context): DeviceInfo {
        return DeviceInfo.getInfo(context)
    }

    /**
     * 需权限 android.permission.READ_PHONE_STATE
     * @return this deviceId
     */
    @SuppressLint("MissingPermission")
    fun getIMEI(context: Context): String? {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.deviceId
    }

    fun getScreenInfo(context: Context): ScreenInfo {

        val metrics = context.resources.displayMetrics
        return ScreenInfo(
                metrics.heightPixels,
                metrics.widthPixels,
                metrics.densityDpi
        )
    }
}