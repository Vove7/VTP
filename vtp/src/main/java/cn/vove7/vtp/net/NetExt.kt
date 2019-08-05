package cn.vove7.vtp.net

/**
 * # NetExt
 * 网络函数扩展
 * @author 11324
 * 2019/4/11
 */

/**
 * 扩展
 * ```
 * Any.post(url){
 *   success {_, e->  }
 *   fail {_, e ->  }
 * }
 * ```
 * @receiver Any?
 * @param url String
 * @param requestCode Int
 * @param callback WrappedRequestCallback<T>.() -> Unit
 */

inline fun <reified T> RequestModel.httpPost(
        url: String, requestCode: Int = 0,
        callback: WrappedRequestCallback<T>.() -> Unit) {
    NetHelper.postJson(url, this, requestCode, callback = callback)
}


inline fun <reified T> Map<String, *>.httpGet(
        url: String, requestCode: Int = 0,
        callback: WrappedRequestCallback<T>.() -> Unit) {
    NetHelper.get(url, this, requestCode, callback)
}

inline fun <reified T> Map<String, *>.httpPost(
        url: String, requestCode: Int = 0,
        callback: WrappedRequestCallback<T>.() -> Unit) {
    NetHelper.get(url, this, requestCode, callback)
}

/**
 * 请求体可继承此接口
 * [RequestModel.httpPost]
 */
interface RequestModel
