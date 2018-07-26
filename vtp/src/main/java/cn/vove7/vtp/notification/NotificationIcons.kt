package cn.vove7.vtp.notification

import android.graphics.drawable.Icon
import android.support.annotation.DrawableRes

/**
 * # NotificationIcons
 *
 * Created by Vove on 2018/7/26
 */
class NotificationIcons {
    @DrawableRes val smallIcon:Int
    var largeIcon: Icon?=null

    constructor(smallIcon: Int, largeIcon: Icon) {
        this.smallIcon = smallIcon
        this.largeIcon = largeIcon
    }

    constructor(smallIcon: Int) {
        this.smallIcon = smallIcon
    }

}