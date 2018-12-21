package cn.vove7.vtp.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import java.util.*

/**
 * # NotificationHelper
 *
 * Usage:
 * //TODO :
 * Created by Vove on 2018/7/26
 */
class NotificationHelper(var context: Context, val channel: NotificationChannel? = null, val alert: Boolean = false) {

    private lateinit var builder: Notification.Builder
    private lateinit var notificationManager: NotificationManager

    private val notificationIds = mutableSetOf<Int>()

    init {
        buildBuilder()
        initNotificationManager()
    }


    private fun initNotificationManager(): NotificationManager {
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && channel != null) {
            notificationManager.createNotificationChannel(channel)
        }
        return notificationManager
    }

    private fun buildBuilder() {
        builder = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Notification.Builder(context, channel?.id)
        } else {
            Notification.Builder(context)
        }
    }

    fun removeAll() {
        synchronized(notificationIds) {
            notificationIds.forEach {
                notificationManager.cancel(it)
            }
            notificationIds.clear()
        }
    }

    fun showNotification(nId: Int, title: String, content: String, icons: NotificationIcons, broadcastIntent: Intent? = null) {

        notificationIds.add(nId)
        builder.setSmallIcon(icons.smallIcon)
                .setContentText(content)
                .setContentTitle(title)
                .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && icons.largeIcon != null) {
            builder.setLargeIcon(icons.largeIcon)
        }
        if (broadcastIntent != null) {
            val contentIntent = PendingIntent
                    .getBroadcast(context, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            builder.setContentIntent(contentIntent)
        }
        val notification = builder.build().apply {
            if (alert && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                vibrate = longArrayOf(0, 200)
            }
        }
        notificationManager.notify(nId, notification)
    }

    fun sendNewNotification(title: String, content: String, icons: NotificationIcons, broadcastIntent: Intent? = null) {
        val nId = Random().nextInt()
        showNotification(nId, title, content, icons, broadcastIntent)
    }

    /**
     * 更新下载进度
     * @param id    id
     * @param intent 下载完成后的广播intent
     */
    fun notifyDownloadProgress(id: Int, title: String, msg: String, max: Int, progress: Int, intent: Intent?) {

        builder.setOngoing(progress != max)
        builder.setSmallIcon(if (max == progress)
            android.R.drawable.stat_sys_download_done
        else
            android.R.drawable.stat_sys_download)
                .setContentText(msg)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setProgress(max, progress, progress < 0)
                .setContentTitle(title)
        if (intent != null) {
            val contentIntent = PendingIntent.getBroadcast(context, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT)
            builder.setContentIntent(contentIntent)
        }
        val notification = builder.build()
        notificationManager.notify(id, notification)
    }

}
