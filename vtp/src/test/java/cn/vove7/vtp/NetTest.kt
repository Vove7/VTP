package cn.vove7.vtp

import cn.vove7.vtp.log.Vog
import cn.vove7.vtp.net.GsonHelper
import cn.vove7.vtp.net.NetHelper
import cn.vove7.vtp.net.WrappedRequestCallback
import cn.vove7.vtp.net.httpGet
import okhttp3.*
import org.junit.Test
import java.io.Serializable
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * # NetTest
 *
 * @author 11324
 * 2019/3/18
 */
class NetTest {

    @Test
    fun main() {
        val l = CountDownLatch(1)
        val call = NetHelper.get<String>("https://www.baidu.com/") {
            success { _, s ->
                println(s)
            }
            fail { _, e ->
                println(e.message)
            }
            before { println("before") }
            end { l.countDown() }

            cancel { println("取消") }
        }
        l.await() //等待请求结束

        //可以终止 request
        call.cancel()
    }


    @Test
    fun testRequest() {

        request<String>("http://localhost:8080/test") {
            success { _, data ->
                print(data)
            }
            fail { _, e ->
                e.printStackTrace()
            }
        }
    }

    inline fun <reified T> request(
            url: String, model: Any? = null,
            callback: WrappedRequestCallback<T>.() -> Unit
    ): Call {
        val client = OkHttpClient.Builder()
                .readTimeout(NetHelper.timeout, TimeUnit.SECONDS).build()

        val json = GsonHelper.toJson(model)
        val requestBody = FormBody.create(MediaType
                .parse("application/json; charset=utf-8"), json)

        val key = "#A$^&J%C"//加密key

        val sign = md5(json + key)

        Vog.d("post ($url)\n$json")
        val request = Request.Builder().url(url)
                .post(requestBody)//请求体
                .addHeader("sign", sign)//请求头加入sign
                .build()

        val call = client.newCall(request)
        NetHelper.call(url, call, 0, callback)
        return call
    }

    fun md5(str: String): String {
        try {
            val instance: MessageDigest = MessageDigest.getInstance("MD5")//获取md5加密对象
            val digest: ByteArray = instance.digest(str.toByteArray())//对字符串加密，返回字节数组
            val sb = StringBuffer()
            for (b in digest) {
                val i: Int = b.toInt() and 0xff//获取低八位有效值
                var hexString = Integer.toHexString(i)//将整数转化为16进制
                if (hexString.length < 2) {
                    hexString = "0$hexString"//如果是一位的话，补0
                }
                sb.append(hexString)
            }
            return sb.toString()

        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        return ""
    }


    @Test
    fun getLastInfo() {
        val lock = Object()
        WrapperNetHelper.get<M>(
                "http://127.0.0.1:4000/1.json") {
            success { _, b ->
                println(b)
                lock.notifyASync()
            }
            fail { _, e ->
                e.printStackTrace()
                lock.notifyASync()
            }
        }

        lock.waitASync()
        print("结束")
    }

    @Test
    fun testExt() {
        val lock = Object()
        mapOf(Pair("1", 1)).httpGet<ResponseMessage<M>>("http://127.0.0.1:4000/1.json") {
            success { i, responseModel ->
                if (responseModel.isOk()) {//检查请求结果
                    println(responseModel)
                } else {
                    print(responseModel.message)
                }
            }
            fail { _, e ->
                e.printStackTrace()
            }
            end {
                print("是否取消请求" + it.isCanceled)
                lock.notifyASync()
                print("结束")
            }
        }
        lock.waitASync()
    }
}

fun Object.notifyASync() {
    synchronized(this) {
        notify()
    }
}

fun Object.waitASync() {
    synchronized(this) {
        wait()
    }
}

/**
 * 封装示例：请求体封装
 * (RequestModel(sign, timestamp, [body]))
 *        |
 *        |
 *       http
 *        |
 *        ↓
 * ResponseMessage(code,message,[data])
 */
object WrapperNetHelper {

    inline fun <reified T> postJson(
            url: String, model: Any? = null, requestCode: Int = -1, arg1: String? = null,
            crossinline callback: WrappedRequestCallback<ResponseMessage<T>>.() -> Unit): Call {

        return NetHelper.postJson(url, RequestModel(model, arg1), requestCode, callback = callback)

    }

    inline fun <reified T> get(
            url: String, params: Map<String, String>? = null, requestCode: Int = 0,
            callback: WrappedRequestCallback<ResponseMessage<T>>.() -> Unit
    ): Call {

        return NetHelper.get(url, params, requestCode, callback)

    }
}

class RequestModel<T : Any>(var body: T? = null, val arg1: String? = null)
    : Serializable {
    val timestamp = (System.currentTimeMillis() / 1000)
    val userId = -1L
    var sign: String = "" //TODO 签名数据 signData(GsonHelper.toJson(body), userId, timestamp)
    val userToken = null

}


data class M(
        var a: Int? = null,
        var b: String? = null,
        var c: Array<Int>? = null
)

/**
 * User: Vove
 * Date: 2018/7/11
 * Time: 22:35
 */
open class ResponseMessage<T> {
    var code: Int = -1

    var message: String = "null"

    var err: String? = null
    var data: T? = null

    fun isOk(): Boolean {
        return code == CODE_OK
    }

    fun isInvalid(): Boolean {
        return code == CODE_INVALID
    }

    fun tokenIsOutdate(): Boolean {
        return code == CODE_TOKEN_OUT_DATE
    }


    override fun toString(): String {
        return "{code=$code, message=$message, err=$err, data=$data}"
    }

    constructor(code: Int, message: String) {
        this.code = code
        this.message = message
    }

    constructor()

    companion object {

        const val CODE_OK = 0
        const val CODE_FAILED = 1//失败
        const val CODE_SERVER_ERR = 2//出错
        const val CODE_INVALID = 5//无效
        const val CODE_TOKEN_OUT_DATE = 6//token过期

        fun <T> error(err: String?): ResponseMessage<T> {
            return ResponseMessage(CODE_FAILED, err
                ?: "null")
        }
    }

}
