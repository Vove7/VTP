package cn.vove7.vtp.calendar

import android.content.Context
import java.util.*

/**
 *
 */
class CalendarAccount(
        /**
         * 显示名称 对应:
         * CalendarContract.Calendars.CALENDAR_DISPLAY_NAME
         */
        val displayName: String,
        /**
         * 理解为 组，一般为邮箱 : xxx@xx.xx
         * CalendarContract.Calendars.OWNER_ACCOUNT
         */
        val ownerAccount: String,
        /**
         * 账户背景颜色
         * CalendarContract.Calendars.CALENDAR_COLOR
         */
        var accountColor: Int? = null,
        /**
         * CalendarContract.Calendars.CALENDAR_TIME_ZONE
         */
        val timeZoneId: String = TimeZone.getDefault().id,
        /**
         * CalendarContract.Calendars._ID
         */
        var id: Long? = null,
        /**
         * 自动创建时所需的context
         */
        autoCreateContext: Context? = null
) {
    init {
        if (autoCreateContext != null)
            initIfNonExists(autoCreateContext)
    }

    /**
     * 初始化CalendarAccount id.
     * 不存在则添加进账户
     */
    fun initIfNonExists(context: Context) {
        val calendarHelper = CalendarHelper(context, this)
        if (!calendarHelper.hasAccount()) {
            if (accountColor == null) {
                accountColor = CalendarHelper.randomColor(context)
            }
            calendarHelper.addLocalAccount()
        }
    }

    override fun toString(): String {
        return "CalendarAccount(displayName='$displayName', " +
                "ownerAccount='$ownerAccount', accountColor=$accountColor, timeZoneId='$timeZoneId', id=$id)"
    }

}