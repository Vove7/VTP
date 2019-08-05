package cn.vove7.vtp.stepaction

/**
 * # StepAction
 *
 * @author Vove
 * 2019/6/12
 */
typealias ActionFun = (args: Array<*>?) -> Array<*>?

object StepAction {

    fun new(vararg actions: ActionFun) {
        var lastAction: Action? = null
        val aclist = Array(actions.size) {
            val newAct = Action(actions[it])
            newAct.last = lastAction
            lastAction = newAct
            newAct
        }
        aclist.forEach {
            val returnValue = it.last?.returnValue
            it.returnValue = it.action.invoke(returnValue)
        }

    }


}

private class Action(val action: ActionFun) {
    var returnValue: Array<*>? = null
    var last: Action? = null
}