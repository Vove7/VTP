package cn.vove7.vtp.reflect

import java.util.*
import kotlin.jvm.internal.Reflection
import kotlin.reflect.KClass
import kotlin.reflect.full.*

/**
 * # KotlinReflectHelper
 *
 * @author Administrator
 * 2018/11/20
 */
object KotlinReflectHelper : ReflectHelper() {

    /**
     * 反射调用Kotlin类里的Companion函数
     * 支持函数重载调用
     * 注：参数位置严格匹配
     *
     * Companion函数的第一个参数为[Companion]
     * @param cls Class<*> javaClass
     * @param name String 函数名
     * @param args Array<out Any?> 参数
     * @return Any? 返回值
     */
    @Throws
    fun invokeCompanionMethod(cls: Class<*>, name: String, vararg args: Any?): Any? {
//        println("调用函数：  ----> $name(${Arrays.toString(args)})")
        val k = Reflection.createKotlinClass(cls)
        k.companionObject?.declaredFunctions?.forEach eachFun@{ f ->
            //            println("invokeCompanionMethod 匹配函数 ----> ${f.name}(${f.parameters})")
            val ps = f.parameters
            if (f.name == name && ps.size == args.size + 1) {//函数名相同, 参数数量相同
                //匹配函数参数
                ps.subList(1, ps.size).withIndex().forEach {
                    val paramType = it.value.type
                    val userParam = args[it.index]
                    val canNullAndNull = paramType.isMarkedNullable && userParam == null  //isMarkedNullable参数类型是否可空
                    if (!canNullAndNull) {
                        val typeMatch = when {//判断类型匹配
                            paramType.isMarkedNullable -> //参数可空 userParam不空 转不可空类型比较
                                paramType.withNullability(false).isSubtypeOf(userParam!!::class.defaultType)

                            userParam != null -> // 参数不可空 且用户参数不空
                                paramType.isSubtypeOf(userParam::class.defaultType)//类型匹配
                            else -> {
//                                println("invokeCompanionMethod  ----> 参数不可空 用户参数空 不符合")
                                return@eachFun //参数不可空 用户参数空 不符合
                            }
                        }
                        if (!typeMatch) {
//                            println("invokeCompanionMethod  ----> 参数类型不匹配")
                            return@eachFun //匹配下一个函数
                        }
                    }
                    //else 参数可空 给定参数空 过
                }
                val argsWithInstance = arrayOf(k.companionObjectInstance, *args)
                return f.call(*argsWithInstance)
            } else {
//                println("invokeCompanionMethod  ----> 名称or参数数量不同")
            }
        }
        throw Exception("no companion method named : $name")
    }

    /**
     *
     * @param cls Class<*> 类 可通过Class.forName(..) 获取
     * @param name String 函数名
     * @param argPairs Array<out Pair<Class<*>, Any?>> first: 值类型, second: 值
     * @return Any? 函数返回值
     */
    @Throws
    fun invokeCompanion(cls: Class<*>, name: String, vararg argPairs: Pair<Class<*>, Any?>): Any? {
//        println("调用函数：  ----> $name(${Arrays.toString(argPairs)})")
        val types = arrayOfNulls<KClass<*>>(argPairs.size)
        val args = arrayOfNulls<Any>(argPairs.size)
        var i = 0
        argPairs.forEach {
            types[i] = Reflection.createKotlinClass(it.first)
            args[i++] = it.second
        }

        val k = Reflection.createKotlinClass(cls)
        k.companionObject?.declaredFunctions?.forEach eachFun@{ f ->
            //            println("invokeCompanionMethod 匹配函数 ----> ${f.name}()")
            val ps = f.parameters
            if (f.name == name && ps.size == argPairs.size + 1) {//函数名相同, 参数数量相同
                //匹配函数参数
                ps.subList(1, ps.size).withIndex().forEach {
                    val paramType = it.value.type
                    val userType = types[it.index]!!.defaultType
                    val typeMatch = paramType.withNullability(false).isSubtypeOf(userType)
                    if (!typeMatch) {
//                        println("invokeCompanionMethod  ----> 参数类型不匹配")
                        return@eachFun //匹配下一个函数
                    }
                    //else 参数可空 给定参数空 过
                }
                val argsWithInstance = arrayOf(k.companionObjectInstance, *args)
                return f.call(*argsWithInstance)
            } else {
//                println("invokeCompanionMethod  ----> 名称or参数数量不同")
            }
        }
        throw NoSuchMethodException("no companion method named : $name(${Arrays.toString(types)}) in class ${cls.name}")
    }

    /**
     * 调用object类函数
     * @param cls Class<*>
     * @param name String
     * @param argPairs Array<out Pair<Class<*>, Any?>>
     * @return Any?
     */
    @Throws
    fun invokeObjectMethod(cls: Class<*>, name: String, vararg argPairs: Pair<Class<*>, Any?>): Any? {
        val k = Reflection.createKotlinClass(cls)
        if (k.objectInstance == null) {//非 Object类
            throw java.lang.Exception("$cls is not a object class")
        }
        return invokeMethod(cls, name, k.objectInstance, *argPairs)
    }

}
