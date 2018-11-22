package cn.vove7.vtp.reflect

/**
 * # ReflectHelper
 *
 * @author Vove
 * 2018/8/14
 */
open class ReflectHelper {
    companion object {

        /**
         * 利用反射，实例化一个泛型类
         * @param clazz 类class
         * @param constructArgs 构造函数的参数
         */
        fun <T> newInstance(clazz: Class<*>, constructArgs: Array<Any>): T {
            val argsType = arrayOfNulls<Class<*>>(constructArgs.size)
            var i = 0
            constructArgs.forEach {
                argsType[i++] = (it::class.java)
            }
            val co = clazz.getDeclaredConstructor(*argsType)
            co.isAccessible = true
            return co.newInstance(*constructArgs) as T
        }

        /**
         * 利用反射，实例化一个泛型类
         * @param fullClassName 全路径类名
         * @param constructArgs 构造函数的参数
         */
        fun <T> newInstanceByName(fullClassName: String, constructArgs: Array<Any>): T {
            val cls = Class.forName(fullClassName) as Class
            return newInstance(cls, constructArgs)
        }

        /**
         * 反射调用java类函数
         * 兼容kotlin class
         * kotlin object class 见 [KotlinReflectHelper.invokeObjectMethod]
         * @param cls Class<*>
         * @param name String
         * @param obj Any? 实例 null 为static 函数
         * @param argPairs Array<Any>
         */
        fun invokeMethod(cls: Class<*>, name: String, obj: Any? = null, vararg argPairs: Pair<Class<*>, Any?>): Any? {
            val types = arrayOfNulls<Class<*>>(argPairs.size)
            val args = arrayOfNulls<Any>(argPairs.size)
            var i = 0
            argPairs.forEach {
                types[i] = it.first
                args[i++] = it.second
            }
            val me = cls.getMethod(name, *types)
            return me.invoke(obj, *args)
        }
    }

}