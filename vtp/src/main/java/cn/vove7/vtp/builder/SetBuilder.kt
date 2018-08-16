package cn.vove7.vtp.builder

/**
 * # SetBuilder
 *
 * @author 17719247306
 * 2018/8/16
 */
class SetBuilder {
    private val set = HashSet<Any>()
    fun add(o: Any): SetBuilder {
        set.add(o)
        return this
    }

    fun allAll(s: Array<Any>): SetBuilder {
        set.addAll(s)
        return this
    }

    fun build(): HashSet<Any> = set
}