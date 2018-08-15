package cn.vove7.vtp.calendar

/**
 * # AccountFilter
 *
 * @author Vove
 * 2018/8/9
 */
interface AccountFilter {
    fun onFilter(account:CalendarAccount): Boolean
}