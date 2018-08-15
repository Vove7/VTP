package cn.vove7.vtp.reflect

/**
 * # ReflectHelper
 *
 * @author Vove
 * 2018/8/14
 */
object ReflectHelper {

    /**
     * 利用反射，实例化一个泛型类
     * @param clazz 类class
     * @param constructArgs 构造函数的参数
     */
    fun <T> newInstance(clazz: Class<*>, constructArgs: Array<Any>): T {
        return newInstanceByName(clazz.name, constructArgs)
    }

    /**
     * 利用反射，实例化一个泛型类
     * @param fullClassName 全路径类名
     * @param constructArgs 构造函数的参数
     */
    fun <T> newInstanceByName(fullClassName: String, constructArgs: Array<Any>): T {
        val argsType = arrayOfNulls<Class<*>>(constructArgs.size)
        var i = 0
        constructArgs.forEach {
            argsType[i++] = (it::class.java)
        }

        val cls = Class.forName(fullClassName) as Class
        val co = cls.getDeclaredConstructor(*argsType)
        co.isAccessible = true
        return co.newInstance(*constructArgs) as T
    }


}