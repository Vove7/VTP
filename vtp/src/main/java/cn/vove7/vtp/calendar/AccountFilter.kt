package cn.vove7.vtp.calendar

/**
 * # AccountFilter
 *
 * @author 17719
 * 2018/8/9
 */
interface AccountFilter {
    fun onFilter(account:CalendarAccount): Boolean
}