package cn.vove7.vtp.network

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class DownloadUtil {
    private val okHttpClient = OkHttpClient()


    /**
     * @param url          下载连接
     * @param destFileDir  下载的文件储存目录
     * @param destFileName 下载文件名称
     * @param listener     下载监听
     */

    fun download(url: String, destFileDir: String, destFileName: String,
                 listener: OnDownloadListener) {

        Thread(Runnable {
            val request = Request.Builder()
                    .url(url)
                    .build()

            //异步请求
            okHttpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // 下载失败监听回调
                    listener.onDownloadFailed(e)
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    var inputStream: InputStream? = null
                    val buf = ByteArray(2048)
                    var len = 0
                    var fos: FileOutputStream? = null

                    //储存下载文件的目录
                    val dir = File(destFileDir)
                    if (!dir.exists()) {
                        dir.mkdirs()
                    }
                    val file = File(dir, destFileName)
                    try {
                        inputStream = response.body()!!.byteStream()
                        val total = response.body()!!.contentLength()
                        fos = FileOutputStream(file)
                        var sum: Long = 0
                        len = inputStream!!.read(buf)
                        while (len != -1) {
                            fos.write(buf, 0, len)
                            sum += len.toLong()
                            val progress = (sum * 1.0f / total * 100).toInt()
                            //下载中更新进度条
                            listener.onDownloading(progress)
                            len = inputStream.read(buf)
                        }
                        fos.flush()
                        //下载完成
                        listener.onDownloadSuccess(file)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        listener.onDownloadFailed(e)
                    } finally {
                        try {
                            inputStream?.close()
                            fos?.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                    }
                }
            })
        }).start()
    }


    interface OnDownloadListener {

        /**
         * 下载成功之后的文件
         */
        fun onDownloadSuccess(file: File)

        /**
         * 下载进度
         */
        fun onDownloading(progress: Int)

        /**
         * 下载异常信息
         */

        fun onDownloadFailed(e: Exception)
    }
}