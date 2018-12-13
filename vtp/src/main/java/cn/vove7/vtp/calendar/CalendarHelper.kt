package cn.vove7.vtp.calendar

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.CalendarContract
import cn.vove7.vtp.R
import cn.vove7.vtp.log.Vog

/**
 * 安卓日历事件
 */
class CalendarHelper(private val context: Context, private val account: CalendarAccount) {

    private val contentResolver: ContentResolver = context.contentResolver

    /**
     * 根据[CalendarAccount] 判断是否存在账户
     * @return [CalendarContract.Calendars._ID]
     */
    fun hasAccount(): Boolean {
        val cursor = contentResolver
                .query(Uri.parse(CALENDAR_URL), null,
                        "${CalendarContract.Calendars.CALENDAR_DISPLAY_NAME} = ? and " +
                                "${CalendarContract.Calendars.OWNER_ACCOUNT} =? and " +
                                "${CalendarContract.Calendars.CALENDAR_TIME_ZONE} =? ",
                        arrayOf(account.displayName, account.ownerAccount, account.timeZoneId), null)
        cursor?.use { c ->
            if (c.moveToNext()) {
                account.id = c.getLong(c.getColumnIndex(CalendarContract.Calendars._ID))
                account.accountColor = c.getInt(c.getColumnIndex(CalendarContract.Calendars.CALENDAR_COLOR))
                return true
            }
        }
        return false
    }

    /**
     * 获取所有账户
     * @param filter 筛选接口 去除不符账户
     */
    fun getAllAccount(filter: AccountFilter? = null): MutableList<CalendarAccount> {
        val accounts = mutableListOf<CalendarAccount>()

        val cursor = contentResolver
                .query(Uri.parse(CALENDAR_URL), null, null, null, null)
        cursor.use { c ->
            if (c == null) return accounts

            val indexName = c.getColumnIndex(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME)
            val indexId = c.getColumnIndex(CalendarContract.Calendars._ID)
            val indexTimeZone = c.getColumnIndex(CalendarContract.Calendars.CALENDAR_TIME_ZONE)
            val indexColor = c.getColumnIndex(CalendarContract.Calendars.CALENDAR_COLOR)
            val indexOwner = c.getColumnIndex(CalendarContract.Calendars.OWNER_ACCOUNT)
            var account: CalendarAccount
            while (c.moveToNext()) {
                account = CalendarAccount(
                        c.getString(indexName),
                        c.getString(indexOwner),
                        c.getInt(indexColor),
                        c.getString(indexTimeZone),
                        c.getLong(indexId))
                if (filter != null) {
                    if (filter.onFilter(account))
                        accounts.add(account)
                } else accounts.add(account)
            }
        }
        return accounts
    }

    /**
     * 删除账户
     * 属于该账户的日历事件也会清楚
     */
    fun deleteAccount(): Boolean {
        if (account.id == null) return false
        return deleteAccountById(context, account.id!!)
    }


    /**
     * 添加本地日历账户
     */
    fun addLocalAccount(): Long {
        val value = ContentValues()
        value.put(CalendarContract.Calendars.NAME, context.packageName)

        value.put(CalendarContract.Calendars.ACCOUNT_NAME, account.ownerAccount)
        value.put(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE)
        value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, account.displayName)
        value.put(CalendarContract.Calendars.VISIBLE, 1)
        value.put(CalendarContract.Calendars.CALENDAR_COLOR, account.accountColor)
        value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER)
        value.put(CalendarContract.Calendars.SYNC_EVENTS, 1)
        value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, account.timeZoneId)
        value.put(CalendarContract.Calendars.OWNER_ACCOUNT, account.ownerAccount)
        value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0)

        var calendarUri = Uri.parse(CALENDAR_URL)
        calendarUri = calendarUri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, account.ownerAccount)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE)
                .build()

        val result = contentResolver.insert(calendarUri, value)
        return if (result != null) ContentUris.parseId(result).also { account.id = it } else -1
    }

    /**
     * 添加日历事件
     * @param title String 显示标题
     * @param description String
     * @param beginTime Long 开始时间 millis
     * @param endTime Long 结束时间 millis
     * @param isAlarm Boolean 是否提醒，默认10分钟
     * @return Int 事件id
     */
    fun addCalendarEvent(title: String, description: String, beginTime: Long, endTime: Long, isAlarm: Boolean = false): Int {//提前提醒
        return addCalendarEvent(title, description, beginTime, endTime, if (isAlarm) 10L else null)
    }

    /**
     * 添加日历事件
     * @param title String 显示标题
     * @param description String
     * @param beginTime Long 开始时间 millis
     * @param endTime Long 结束时间 millis
     * @param earlyAlarmMinute Long? 提前提醒时间 单位分钟 , if null 不提醒
     * @return Int 事件id
     */
    fun addCalendarEvent(title: String, description: String, beginTime: Long, endTime: Long, earlyAlarmMinute: Long? = null): Int {//提前提醒

        val event = ContentValues()
        event.put("title", title)
        event.put("description", description)
        // 插入账户的id
        event.put("calendar_id", account.id)

        event.put(CalendarContract.Events.DTSTART, beginTime + 10000)
        event.put(CalendarContract.Events.DTEND, endTime)
        event.put(CalendarContract.Events.HAS_ALARM, if (earlyAlarmMinute != null) 1 else 0) //设置有闹钟提醒
        event.put(CalendarContract.Events.EVENT_COLOR, randomColor(context))
        event.put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Shanghai")  //这个是时区，必须有
        //添加事件
        val newEvent = context.contentResolver.insert(Uri.parse(CALENDAR_EVENT_URL), event)
            ?: // 添加日历事件失败直接返回
            return RESULT_ADD_FAILED
        //事件提醒的设定
        if (earlyAlarmMinute != null) {
            val values = ContentValues()
            values.put(CalendarContract.Reminders.EVENT_ID, ContentUris.parseId(newEvent))
            // 提前10分钟有提醒
            values.put(CalendarContract.Reminders.MINUTES, earlyAlarmMinute)
            values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT)
            context.contentResolver.insert(Uri.parse(CALENDAR_REMINDER_URL), values)
                ?: // 添加闹钟提醒失败直接返回
                return RESULT_ALARM_FAILED
        }
        return RESULT_OK
    }

    /**
     * 根据事件id 删除事件
     * @param eventId Long
     * @return Boolean
     */
    fun deleteEvent(eventId: Long): Boolean {
        val uri = ContentUris.withAppendedId(Uri.parse(CALENDAR_EVENT_URL), eventId)
        return 1 == context.contentResolver.delete(uri, null, null)
    }

    /**
     * 根据事件标题删除事件
     * @param title String
     * @return Boolean
     */
    fun deleteEvent(title: String): Boolean {
        val eventCursor = context.contentResolver
                .query(Uri.parse(CALENDAR_EVENT_URL), null, null, null, null)
        eventCursor.use { cursor ->
            if (cursor != null && cursor.count > 0) {
                //遍历所有事件，找到title跟需要查询的title一样的项
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    val eventTitle = cursor.getString(cursor.getColumnIndex("title"))
                    if (title == eventTitle) {
                        val id = cursor.getInt(cursor.getColumnIndex(CalendarContract.Calendars._ID))//取得id
                        val deleteUri = ContentUris.withAppendedId(Uri.parse(CALENDAR_EVENT_URL), id.toLong())
                        val rows = context.contentResolver.delete(deleteUri, null, null)
                        return rows > 0
                    }
                    cursor.moveToNext()
                }
            }
            return false
        }
    }

    companion object {

        fun deleteAccountById(context: Context, id: Long): Boolean {
            val uri = ContentUris.withAppendedId(Uri.parse(CALENDAR_URL), id)
            return 1 == context.contentResolver.delete(uri, null, null)
        }

        public fun randomColor(context: Context): Int {
            return context.resources.getColor(colors[(Math.random() * (colors.size - 1)).toInt()])
        }

        fun deleteAccountByName(context: Context, displayName: String): Boolean {
            val cursor = context.contentResolver
                    .query(Uri.parse(CALENDAR_URL), null, null, null, null)
            cursor.use { cur ->
                if (cur == null) {
                    return false
                }
                while (cur.moveToNext()) {
                    val name = cur.getString(
                            cur.getColumnIndex(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME))
                    if (displayName == name) {
                        val id = cur.getLong(cur.getColumnIndex(CalendarContract.Calendars._ID))
                        val uri = ContentUris.withAppendedId(Uri.parse(CALENDAR_URL), id)
                        Vog.d(this, "deleteAccountByName: $id $name")
                        return 1 == context.contentResolver.delete(uri, null, null)
                    }
                }
            }
            return false
        }

        private const val CALENDAR_URL = "content://com.android.calendar/calendars"
        private const val CALENDAR_EVENT_URL = "content://com.android.calendar/events"
        private const val CALENDAR_REMINDER_URL = "content://com.android.calendar/reminders"

        /**
         * LOCAL
         */
        private const val CALENDARS_ACCOUNT_TYPE = "LOCAL"

        const val RESULT_OK = 0
        const val RESULT_NO_ACCOUNT = 1
        const val RESULT_ADD_FAILED = 2
        const val RESULT_ALARM_FAILED = 3

        val colors = arrayOf(
                R.color.teal_A700,
                R.color.brown_800,
                R.color.orange_A700,
                R.color.deep_purple_A700,
                R.color.pink_A400,
                R.color.red_500,
                R.color.deep_purple_700,
                R.color.blue_500,
                R.color.light_blue_500,
                R.color.cyan_500,
                R.color.green_700,
                R.color.light_green_500,
                R.color.lime_600,
                R.color.yellow_A400,
                R.color.amber_500,
                R.color.orange_700,
                R.color.deep_orange_A400,
                R.color.blue_grey_500
        )
    }
}
