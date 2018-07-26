package cn.vove7.vtp.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.support.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.O)
/**
 * ChannelBuilder
 * 8.0 通知适配
 *
 *
 * Created by Vove on 2018/7/26
 */
class ChannelBuilder(var channel: NotificationChannel) {
    companion object {
        /**
         * # buildChannel
         *
         * @return
         */
        @RequiresApi(Build.VERSION_CODES.O)
        fun with(id: String, name: String,
                 level: Int = NotificationManager.IMPORTANCE_DEFAULT): ChannelBuilder {
            val channel = NotificationChannel(id, name, level)
            channel.enableLights(true)
            channel.setShowBadge(true)
            return ChannelBuilder(channel)
        }
    }

    fun lightColor(color: Int): ChannelBuilder {
        channel.lightColor = color
        return this
    }

    fun build(): NotificationChannel {
        return channel
    }
}