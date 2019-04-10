package cn.vove7.vtp.net

import android.os.Looper
import cn.vove7.vtp.extend.runOnUi
import cn.vove7.vtp.log.Vog
import okhttp3.*
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread


/**
 * # NetHelper
 * 网络请求相关
 *
 * 请求示例:
 * ```
 *   NetHelper.get<String>("http://baidu.com") {
 *      success { _, s ->
 *          println(s)
 *      }
 *      fail { _, e ->
 *          e.printStackTrace()
 *      }
 *   }
 * ```
 *
 * @author 1132412166
 * 2018/9/12
 */
@Suppress("unused")
object NetHelper {

    var timeout = 15L

    inline fun <reified T> get(
            url: String, params: Map<String, String>? = null, requestCode: Int = 0,
            callback: WrappedRequestCallback<T>.() -> Unit
    ): Call {

        val client = OkHttpClient.Builder()
                .readTimeout(timeout, TimeUnit.SECONDS).build()

        val request = Request.Builder().url(url)
                .get().apply {
                    params?.forEach {
                        addHeader(it.key, it.value)
                    }
                }
                .build()
        val call = client.newCall(request)
        call(call, requestCode, callback)
        return call
    }


    /**
     * post文件
     * @param url String
     * @param model Any?  被放至 json_data 字段中
     * @param requestCode Int
     * @param callback WrappedRequestCallback<T>.()
     */
    inline fun <reified T> postFile(
            url: String, model: Any? = null,
            requestCode: Int = 0, files: Array<File>,
            filePartName: String = "multipartFile",
            callback: WrappedRequestCallback<T>.() -> Unit
    ): Call {
        val client = OkHttpClient.Builder()
                .readTimeout(timeout, TimeUnit.SECONDS).build()

        val multiBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
        val mediaType = MediaType.parse("application/octet-stream");//设置类型，类型为八位字节流

        if (model != null) {
            val json = GsonHelper.toJson(model)
            multiBuilder.addFormDataPart("json_data", json)//json
        }


        //文件
        Vog.d("postFile 文件个数 ----> ${files.size}")
        files.forEach {
            val fileBody = RequestBody.create(mediaType, it)//把文件与类型放入请求体
            multiBuilder.addFormDataPart(filePartName, it.name, fileBody)
        }
        val requestBody = multiBuilder.build()
        val request = Request.Builder().url(url)
                .post(requestBody)
                .build()
        val call = client.newCall(request)
        call(call, requestCode, callback)
        return call

    }

    /**
     * 网络post请求 内容格式为json
     * @param url String
     * @param model Any? 请求体
     * @param requestCode Int
     * @param callback WrappedRequestCallback<T>.()
     */
    inline fun <reified T> postJson(
            url: String, model: Any? = null, requestCode: Int = 0,
            callback: WrappedRequestCallback<T>.() -> Unit
    ): Call {
        val client = OkHttpClient.Builder()
                .readTimeout(timeout, TimeUnit.SECONDS).build()

        val json = GsonHelper.toJson(model)
        val requestBody = FormBody.create(MediaType
                .parse("application/json; charset=utf-8"), json)

        Vog.d("post ($url)\n$json")
        val request = Request.Builder().url(url)
                .post(requestBody)
                .build()
        val call = client.newCall(request)
        call(call, requestCode, callback)
        return call
    }

    inline fun <reified T> call(
            call: Call, requestCode: Int = 0,
            cb: WrappedRequestCallback<T>.() -> Unit
    ) {
        val callback = WrappedRequestCallback<T>()
        cb.invoke(callback)
        thread(isDaemon = true) {
            prepareIfNeeded()
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onBefore()
                    e.printStackTrace()

                    callback.onFailed(requestCode, e)
                    callback.onEnd()
                }

                override fun onResponse(call: Call, response: Response) {//响应成功更新UI
                    val s = response.body()?.string()
                    callback.onBefore()
                    if (response.isSuccessful) {
                        try {
                            Vog.d("onResponse (${call.request().url()})--->\n$s")
                            val bean: T = if (T::class.java == String::class.java) {
                                (s ?: "") as T
                            } else
                                GsonHelper.fromJson<T>(s)!!
                            callback.onSuccess(requestCode, bean)

                        } catch (e: Exception) {
                            e.printStackTrace()
                            callback.onFailed(requestCode, e)
                        }
                    } else callback.onFailed(requestCode, Exception("请求失败${response.message()}"))

                    callback.onEnd()
                }
            })
        }
    }


    fun prepareIfNeeded() {
        try {
            if (Looper.myLooper() == null)
                Looper.prepare()
        } catch (e: Exception) {
        }
    }

}

interface RequestCallback<T> {
    fun onSuccess(requestCode: Int, data: T)
    fun onFailed(requestCode: Int, e: Exception)
    fun onBefore()
    fun onEnd()

}

class WrappedRequestCallback<T> : RequestCallback<T> {
    private var _OnSuccess: ((Int, T) -> Unit)? = null
    private var _OnFailed: ((Int, Exception) -> Unit)? = null
    private var _OnBefore: (() -> Unit)? = null
    private var _OnEnd: (() -> Unit)? = null

    companion object {
        var errListener: ((e: Throwable) -> Unit)? = null
    }

    override fun onSuccess(requestCode: Int, data: T) {
        runOnUi {
            _OnSuccess?.invoke(requestCode, data)
        }
    }

    override fun onFailed(requestCode: Int, e: Exception) {
        errListener?.invoke(e)
        runOnUi {
            _OnFailed?.invoke(requestCode, e)
        }
    }

    override fun onBefore() {
        runOnUi {
            _OnBefore?.invoke()
        }
    }

    override fun onEnd() {
        runOnUi {
            _OnEnd?.invoke()
        }
    }

    fun before(b: () -> Unit) {
        _OnBefore = b
    }

    fun end(e: () -> Unit) {
        _OnEnd = e
    }

    fun success(s: (Int, T) -> Unit) {
        _OnSuccess = s
    }

    fun fail(s: (Int, Exception) -> Unit) {
        _OnFailed = s
    }
}
