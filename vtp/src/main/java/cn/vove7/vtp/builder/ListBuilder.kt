package cn.vove7.vtp.builder

/**
 * # ListBuilder
 *
 * @author 17719247306
 * 2018/8/16
 */
class ListBuilder {
    private val list = ArrayList<Any>()
    fun add(o: Any): ListBuilder {
        list.add(o)
        return this
    }

    fun allAll(s: Array<Any>): ListBuilder {
        list.addAll(s)
        return this
    }

    fun build(): ArrayList<Any> = list
}