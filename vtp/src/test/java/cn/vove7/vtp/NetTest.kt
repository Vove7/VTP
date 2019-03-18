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
        NetHelper.get<String>("https://www.baidu.com/") {
            success { _, s ->
                println(s)
            }
            fail { _, e ->
                e.printStackTrace()
            }
        }
        sleep(5000)
    }

}