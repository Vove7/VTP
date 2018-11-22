package cn.vove7.vtp

import cn.vove7.vtp.reflect.KotlinReflectHelper
import org.junit.Assert
import org.junit.Test

/**
 * # ReflectTest
 *
 * @author Administrator
 * 2018/11/22
 */
class ReflectTest {

    @Test
    fun kotlinReflectTest() {
        val cls = Class.forName("cn.vove7.vtp.C")
        KotlinReflectHelper.invokeCompanionMethod(cls, "src/main/a")//无参数函数
        KotlinReflectHelper.invokeCompanionMethod(cls, "b", "sss")//可空参数函数
        KotlinReflectHelper.invokeCompanionMethod(cls, "b", null)//可空参数函数
        KotlinReflectHelper.invokeCompanionMethod(cls, "b", 1)//重载函数
        val s = KotlinReflectHelper.invokeCompanionMethod(cls, "c")//返回值函数
        println(s)

        var r = KotlinReflectHelper.invokeCompanionMethod(cls, "e", 1, 2, "求和")//多参数函数
        println(r)
        r = KotlinReflectHelper.invokeCompanionMethod(cls, "e", 1, 2, null)//多参数函数
        println(r)

    }

    @Test
    fun kotlinReflectTest2() {
        val cls = Class.forName("cn.vove7.vtp.C")
        KotlinReflectHelper.invokeCompanion(cls, "src/main/a")//无参数函数
        KotlinReflectHelper.invokeCompanion(cls, "b", Pair(String::class.java, "sss"))//可空参数函数
        KotlinReflectHelper.invokeCompanion(cls, "b", Pair(Int::class.java, null))//可空参数函数
        KotlinReflectHelper.invokeCompanion(cls, "b", Pair(Int::class.java, 1))//重载函数
        val s = KotlinReflectHelper.invokeCompanion(cls, "c")//返回值函数
        println(s)

        var r = KotlinReflectHelper.invokeCompanion(cls, "e", Pair(Int::class.java, 1),
                Pair(Int::class.java, 2), Pair(String::class.java, "求和"))//多参数函数
        println(r)
        r = KotlinReflectHelper.invokeCompanion(cls, "e", Pair(Int::class.java, 1),
                Pair(Int::class.java, 2), Pair(String::class.java, null))//多参数函数
        println(r)
    }

    @Test
    fun invokeObjectClassMethod() {
        val cls = Class.forName("cn.vove7.vtp.D")
        KotlinReflectHelper.invokeObjectMethod(cls, "hello")
    }
}

/**
 * kotlin object class
 */
object D {
    fun hello() {
        println("d:hello")
    }
}

class C {
    companion object {
        fun a() {
            println("src/main/a")
        }

        fun b(s: String?) {
            println("b string $s")
        }

        fun b(a: Int?) {
            println("b int $a")
        }

        fun c(): String {
            return "c 12345"
        }

        fun e(a: Int, b: Int, c: String?): String {
            return "$c $a + $b = ${a + b}"
        }

    }
}