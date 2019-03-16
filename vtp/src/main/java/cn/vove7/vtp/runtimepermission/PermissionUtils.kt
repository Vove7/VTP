package cn.vove7.vtp.runtimepermission

import android.accessibilityservice.AccessibilityServiceInfo
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.accessibility.AccessibilityManager
import cn.vove7.vtp.log.Vog


object PermissionUtils {

    /**
     * @return 无障碍服务是否开启
     *
     * 无障碍 info.id 格式 appId/serverName
     */
    fun accessibilityServiceEnabled(context: Context): Boolean {
        val pkg = context.packageName
        val am = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledAccessibilityServiceList = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC)
        for (info in enabledAccessibilityServiceList) {
            Vog.v(info.id)
            if (info.id.startsWith("$pkg/"))
                return true

        }
        return false
    }

    /**
     * 跳转至无障碍设置
     */
    fun gotoAccessibilitySetting(context: Context) {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    /**
     * @return 通知使用权限是否开启
     */
    fun notificationListenerEnabled(context: Context): Boolean {
        val enable: Boolean
        val packageName = context.packageName
        val flat = Settings.Secure.getString(context.contentResolver, "enabled_notification_listeners")
        Vog.v( "flat - $flat")
        enable = flat.contains(packageName)
        return enable
    }

    /**
     * 跳转通知使用权限
     */
    @Throws(ActivityNotFoundException::class)
    fun gotoNotificationAccessSetting(context: Context) {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
        } else {
            Intent().apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                val cn = ComponentName("com.android.settings", "com.android.settings.Settings\$NotificationAccessSettingsActivity")
                component = cn
                putExtra(":settings:show_fragment", "NotificationAccessSettings")
            }
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    /**
     * 自动请求多个权限
     * requestCode 为权限数组index
     * 重写父类 onRequestPermissionsResult
     * @see AppCompatActivity#onRequestPermissionsResult()
     * @param requestCode default 1
     * @return 返回是否全部被授权
     *
     * help:
     *  PackageManager.PERMISSION_GRANTED 表示有权限，
     *  PackageManager.PERMISSION_DENIED 表示无权限。
     *
     */
    fun autoRequestPermission(activity: Activity, permissions: Array<String>, requestCode: Int = 1): Boolean {
        val noPermission = arrayListOf<String>()
        permissions.forEach {
            if (ActivityCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED)
                noPermission.add(it)
        }
        return if (noPermission.size > 0) {
            ActivityCompat.requestPermissions(activity, noPermission.toTypedArray(), requestCode)
            false
        } else {
            true
        }
    }


    /**
     * @return 是否全部授权
     * 可用于查询必须权限是否全部被授权
     */
    fun isAllGranted(context: Context, permissions: Array<String>): Boolean {
        permissions.forEach {
            if (ActivityCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED)
                return false
        }
        return true
    }

    /**
     * onRequestPermissionsResult返回结果
     * @return 是否全部授权
     */
    fun isAllGranted(grantResults: IntArray): Boolean {
        grantResults.forEach {
            if (it != PackageManager.PERMISSION_GRANTED)
                return false
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun canDrawOverlays(context: Context): Boolean {
        return Settings.canDrawOverlays(context)
    }

    /**
     * 请求悬浮窗
     *
     * <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
     * 复写onActivityResult
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun requestDrawOverlays(activity: Activity, reqCode: Int) {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + activity.packageName))
        activity.startActivityForResult(intent, reqCode, null)
    }


}
