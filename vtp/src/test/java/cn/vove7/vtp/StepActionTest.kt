package cn.vove7.vtp

import cn.vove7.vtp.stepaction.ActionFun
import cn.vove7.vtp.stepaction.StepAction
import org.junit.Test
import java.util.*

/**
 * # StepActionTest
 *
 * @author Vove
 * 2019/6/12
 */
class StepActionTest {

    @Test
    fun main() {
        var i = 0
        val action: ActionFun = { a ->
            println(Arrays.toString(a))
            arrayOf(i++)
        }


        StepAction.new(action, action, action)
    }
}