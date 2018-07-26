package cn.vove7.vtp.badge

import android.content.ComponentName
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log

/**
 * 适配桌面通知圆点
 */

class BadgeUtil @Throws(InstantiationException::class)
private constructor() {

    init {
        throw InstantiationException("This class is not for instantiation")
    }

    companion object {
        var NUM = 0

        fun subCount(context: Context, n: Int) {
            NUM -= n
            setBadgeCount(context, NUM)
        }

        fun resetBadgeCount(context: Context) {
            setBadgeCount(context, 0)
        }

        /**
         * 设置Badge 目前支持Launcher
         */
        fun setBadgeCount(context: Context, count: Int) {
            var count = count
            NUM = count
            count = if (count <= 0) 0
            else Math.max(0, Math.min(count, 99))


            if (Build.MANUFACTURER.equals("xiaomi", ignoreCase = true)) {
                //setXiaoMiBadge(context, count);
            } else if (Build.MANUFACTURER.equals("sony", ignoreCase = true)) {
                setBadgeOfSony(context, count)
            } else if (Build.MANUFACTURER.toLowerCase().contains("samsung") || Build.MANUFACTURER.toLowerCase().contains("lg")) {
                setBadgeOfSumsung(context, count)
            } else if (Build.MANUFACTURER.toLowerCase().contains("htc")) {
                setBadgeOfHTC(context, count)
            } else if (Build.MANUFACTURER.toLowerCase().contains("nova")) {
                setBadgeOfNova(context, count)
            } else if (Build.MANUFACTURER.toLowerCase().contains("huawei")) {
                setBadgeNum(context, count)
            } else {
                Log.d("Vove :", "BadgeUtil setBadgeCount  ----> Not Found Support Launcher ")
                //Toast.makeText(context, "Not Found Support Launcher", Toast.LENGTH_LONG).show();
            }
        }


        /**
         * set badge number
         */
        private fun setBadgeNum(context: Context, num: Int) {
            try {
                val bunlde = Bundle()
                bunlde.putString("package", context.packageName)
                bunlde.putString("class", getLauncherClassName(context)) //main activity
                bunlde.putInt("badgenumber", num)
                context.contentResolver.call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", null, bunlde)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        /**
         * 设置MIUI的Badge
         */
        private fun setXiaoMiBadge(context: Context, number: Int) {
            //        try{
            //            Class miuiNotificationClass = Class.forName("android.app.MiuiNotification");
            //            Object miuiNotification = miuiNotificationClass.newInstance();
            //            Field field = miuiNotification.getClass().getDeclaredField("messageCount");
            //            field.setAccessible(true);
            //            field.set(miuiNotification, String.valueOf(number==0?"":number));
            //        }catch (Exception e){
            //            e.printStackTrace();
            //            // miui6之前
            //            Intent localIntent = new Intent("android.intent.action.APPLICATION_MESSAGE_UPDATE");
            //            localIntent.putExtra("android.intent.extra.update_application_component_name",context.getPackageName()+"/."+"MainActivity");
            //            localIntent.putExtra("android.intent.extra.update_application_message_text", String.valueOf(number==0?"":number));
            //            context.sendBroadcast(localIntent);
            ////        }
            //      NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            //      Intent appIntent = null;
            //      int notificationId = 0x1234;
            //      Notification.Builder builder = new Notification.Builder(context, "1"); //与channelId对应
            //      //icon title text必须包含，不然影响桌面图标小红点的展示
            //
            //      builder.setSmallIcon(android.R.drawable.stat_notify_chat).setContentTitle("通知").setContentText("通知内容").setNumber(3).setAutoCancel(true); //久按桌面图标时允许的此条通知的数量
            //      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //         NotificationChannel channel = new NotificationChannel("1", "Channel1", NotificationManager.IMPORTANCE_DEFAULT);
            //         channel.enableLights(true); //是否在桌面icon右上角展示小红点
            //         channel.setLightColor(Color.GREEN); //小红点颜色
            //         channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
            //
            //         notificationManager.createNotificationChannel(channel);
            //      }
            //      notificationManager.notify(notificationId, builder.build());


        }

        /**
         * 设置索尼的Badge
         * 需添加权限：<uses-permission android:name="com.sonyericsson.home.permission.BROADCAST_BADGE"></uses-permission>
         */
        private fun setBadgeOfSony(context: Context, count: Int) {
            val launcherClassName = getLauncherClassName(context) ?: return
            var isShow = true
            if (count == 0) {
                isShow = false
            }
            val localIntent = Intent()
            localIntent.action = "com.sonyericsson.home.action.UPDATE_BADGE"
            localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE", isShow)//是否显示
            localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME", launcherClassName)//启动页
            localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", count.toString())//数字
            localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME", context.packageName)//包名
            context.sendBroadcast(localIntent)
        }

        /**
         * 设置三星的Badge\设置LG的Badge
         */
        private fun setBadgeOfSumsung(context: Context, count: Int) {
            // 获取你当前的应用
            val launcherClassName = getLauncherClassName(context) ?: return
            val intent = Intent("android.intent.action.BADGE_COUNT_UPDATE")
            intent.putExtra("badge_count", count)
            intent.putExtra("badge_count_package_name", context.packageName)
            intent.putExtra("badge_count_class_name", launcherClassName)
            context.sendBroadcast(intent)
        }

        /**
         * 设置HTC的Badge
         */
        private fun setBadgeOfHTC(context: Context, count: Int) {
            val intentNotification = Intent("com.htc.launcher.action.SET_NOTIFICATION")
            val localComponentName = ComponentName(context.packageName, getLauncherClassName(context)!!)
            intentNotification.putExtra("com.htc.launcher.extra.COMPONENT", localComponentName.flattenToShortString())
            intentNotification.putExtra("com.htc.launcher.extra.COUNT", count)
            context.sendBroadcast(intentNotification)

            val intentShortcut = Intent("com.htc.launcher.action.UPDATE_SHORTCUT")
            intentShortcut.putExtra("packagename", context.packageName)
            intentShortcut.putExtra("count", count)
            context.sendBroadcast(intentShortcut)
        }

        /**
         * 设置Nova的Badge
         */
        private fun setBadgeOfNova(context: Context, count: Int) {
            val contentValues = ContentValues()
            contentValues.put("tag", context.packageName + "/" + getLauncherClassName(context))
            contentValues.put("count", count)
            context.contentResolver.insert(Uri.parse("content://com.teslacoilsw.notifier/unread_count"),
                    contentValues)
        }

        fun setBadgeOfMadMode(context: Context, count: Int, packageName: String, className: String) {
            val intent = Intent("android.intent.action.BADGE_COUNT_UPDATE")
            intent.putExtra("badge_count", count)
            intent.putExtra("badge_count_package_name", packageName)
            intent.putExtra("badge_count_class_name", className)
            context.sendBroadcast(intent)
        }


        private fun getLauncherClassName(context: Context): String? {
            val packageManager = context.packageManager
            val intent = Intent(Intent.ACTION_MAIN)
            intent.setPackage(context.packageName)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            var info: ResolveInfo? = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
            if (info == null) {
                info = packageManager.resolveActivity(intent, 0)
            }
            Log.d("Vove :", "BadgeUtil getLauncherClassName  ----> " + info!!.activityInfo.name)
            return info.activityInfo.name
        }
    }
}
