package cn.vove7.vtp.system

import android.content.Context
import android.os.Build
import java.util.*

/**
 * # DeviceInfo
 *
 * @author Vove
 * 2018/8/15
 */
data class DeviceInfo(
        var IMEI: String? = null,//IMEI
        var screenSInfo: ScreenInfo?,//屏幕信息
        var manufacturerName: String,//厂商名
        var productName: String,//产品名
        var brandName: String,//品牌
        var model: String,//型号
        var boardName: String,//主板名
        var deviceName: String,//设备名
        var serial: String,//序列号
        var sdkInt: Int,//Sdk版本
        var androidVersion: String,//Android版本

        var language: String//当前语言
) {
    companion object {
        private var instance: DeviceInfo? = null
        fun getInfo(context: Context, needImei: Boolean = false): DeviceInfo {
            if (instance == null) {
                init(context, needImei)
            } else {
                instance!!.language = Locale.getDefault().language
            }
            return instance!!
        }

        private fun init(context: Context, b: Boolean) {
            instance = DeviceInfo(
                    if (b) SystemHelper.getIMEI(context) else null,
                    SystemHelper.getScreenInfo(context),
                    Build.MANUFACTURER,
                    Build.PRODUCT,
                    Build.BRAND,
                    Build.MODEL,
                    Build.BOARD,
                    Build.DEVICE,
                    Build.SERIAL,
                    Build.VERSION.SDK_INT,
                    Build.VERSION.RELEASE,
                    Locale.getDefault().language
            )
        }
    }
}
