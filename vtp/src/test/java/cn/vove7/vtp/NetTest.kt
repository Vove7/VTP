package cn.vove7.vtp

import cn.vove7.vtp.net.NetHelper
import org.junit.Test
import java.lang.Thread.sleep

/**
 * # NetTest
 *
 * @author 11324
 * 2019/3/18
 */
class NetTest {

    @Test
    fun main() {
        val call = NetHelper.get<String>("https://www.baidu.com/") {
            success { _, s ->
                println(s)
            }
            fail { _, e ->
                println(e.message)
            }
            before { println("before") }
            end { println("end") }
            cancel { println("取消") }
        }
        sleep(10)
//        call.cancel()
        sleep(10000)
    }

}