package cn.vove7.vtp.app

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v4.content.FileProvider
import android.util.Log
import java.io.File
import java.util.*

/**
 # OpenFileUtil
 - 调用系统软件打开文件
 - 适配安卓7.0+ ↓
 * 1. 以下添加到`AndroidManifest.xml`的`application`级下
```
<provider
android:name="android.support.v4.content.FileProvider"
android:authorities="${applicationId}.fileprovider"
android:exported="false"
android:grantUriPermissions="true">
<meta-data
android:name="android.support.FILE_PROVIDER_PATHS"
android:resource="@xml/file_paths" />
</provider>
```
 * 2.在res/xml创建file_paths.xml文件
```
<?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
<external-path name="path_name" path="目录名"/>
.
.
.
</paths>
```
- 参考
<files-path/>代表的根目录： Context.getFilesDir()
<external-path/>代表的根目录: Environment.getExternalStorageDirectory()
<cache-path/>代表的根目录: getCacheDir()


 */
object OpenFileUtil {

    fun openFile(context: Context, file: File) {
        val filePath = file.absolutePath
        Log.d("Vove :", "openFile  ----> $filePath")
        if (!file.exists()) {
            Log.e("openFile", "openFile: 文件不存在$filePath")
            return
        }
        /* 取得扩展名 */
        val end = file.name.substring(file.name.lastIndexOf(".") + 1, file.name.length).toLowerCase(Locale.getDefault())
        /* 依扩展名的类型决定MimeType */
        var b = false
        val intent = when (end) {
            "m4a", "mp3", "mid", "xmf", "ogg", "wav" -> getAudioFileIntent(context, filePath)
            "3gp", "mp4" -> getVideoFileIntent(context, filePath)
            "jpg", "gif", "png", "jpeg", "bmp" -> getImageFileIntent(context, filePath)
            "apk" -> getApkFileIntent(context, filePath)
            "ppt" -> getPptFileIntent(context, filePath)
            "xls" -> getExcelFileIntent(context, filePath)
            "doc" -> getWordFileIntent(context, filePath)
            "pdf" -> getPdfFileIntent(context, filePath)
            "chm" -> getChmFileIntent(context, filePath)
            "txt" -> getTextFileIntent(context, filePath, false)
            "html", "htm" -> getHtmlFileIntent(context, filePath)
            else -> {
                b = true
                getAllIntent(context, filePath)
            }
        }
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            if (!b) {
                context.startActivity(getAllIntent(context, filePath))
            }
            e.printStackTrace()
        }

    }

    fun openFile(context: Context, filePath: String) {
        openFile(context, File(filePath))
    }

    // Android获取一个用于打开APK文件的intent
    private fun getAllIntent(context: Context, param: String): Intent {
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = Intent.ACTION_VIEW
        intent.setDataAndType(getUri(context, intent, File(param)), "*/*")
        return intent
    }

    // Android获取一个用于打开APK文件的intent
    private fun getApkFileIntent(context: Context, param: String): Intent {

        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = Intent.ACTION_VIEW
        intent.setDataAndType(getUri(context, intent, File(param)), "application/vnd.android.package-archive")
        return intent
    }

    // Android获取一个用于打开VIDEO文件的intent
    private fun getVideoFileIntent(context: Context, param: String): Intent {

        val intent = Intent("android.intent.action.VIEW")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("oneshot", 0)
        intent.putExtra("configchange", 0)
        intent.setDataAndType(getUri(context, intent, File(param)), "video/*")
        return intent
    }

    // Android获取一个用于打开AUDIO文件的intent
    private fun getAudioFileIntent(context: Context, param: String): Intent {

        val intent = Intent("android.intent.action.VIEW")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("oneshot", 0)
        intent.putExtra("configchange", 0)
        intent.setDataAndType(getUri(context, intent, File(param)), "audio/*")
        return intent
    }

    // Android获取一个用于打开Html文件的intent
    private fun getHtmlFileIntent(context: Context, param: String): Intent {
        val intent = Intent("android.intent.action.VIEW")
        val uri = getUri(context, intent, File(param))
                .buildUpon().encodedAuthority("com.android.htmlfileprovider")
                .scheme("content").encodedPath(param).build()
        intent.setDataAndType(uri, "text/html")
        return intent
    }

    // Android获取一个用于打开图片文件的intent
    private fun getImageFileIntent(context: Context, param: String): Intent {

        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setDataAndType(getUri(context, intent, File(param)), "image/*")
        return intent
    }

    // Android获取一个用于打开PPT文件的intent
    private fun getPptFileIntent(context: Context, param: String): Intent {

        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setDataAndType(getUri(context, intent, File(param)), "application/vnd.ms-powerpoint")
        return intent
    }

    // Android获取一个用于打开Excel文件的intent
    private fun getExcelFileIntent(context: Context, param: String): Intent {

        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setDataAndType(getUri(context, intent, File(param)), "application/vnd.ms-excel")
        return intent
    }

    // Android获取一个用于打开Word文件的intent
    private fun getWordFileIntent(context: Context, param: String): Intent {

        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setDataAndType(getUri(context, intent, File(param)), "application/msword")
        return intent
    }

    // Android获取一个用于打开CHM文件的intent
    private fun getChmFileIntent(context: Context, param: String): Intent {

        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setDataAndType(getUri(context, intent, File(param)), "application/x-chm")
        return intent
    }

    // Android获取一个用于打开文本文件的intent
    private fun getTextFileIntent(context: Context, param: String, paramBoolean: Boolean): Intent {

        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (paramBoolean) {
            val uri1 = Uri.parse(param)
            intent.setDataAndType(uri1, "text/plain")
        } else {
            intent.setDataAndType(getUri(context, intent, File(param)), "text/plain")
        }
        return intent
    }

    // Android获取一个用于打开PDF文件的intent
    private fun getPdfFileIntent(context: Context, param: String): Intent {

        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setDataAndType(getUri(context, intent, File(param)), "application/pdf")
        return intent
    }

    /**
     * 获取对应文件的Uri
     * 适配7.0+
     *
     * @param intent 相应的Intent
     * @param file   文件对象
     * @return
     */
    private fun getUri(context: Context, intent: Intent, file: File): Uri {
        val uri: Uri
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //判断版本是否在7.0以上
            uri = FileProvider
                    .getUriForFile(context, context.packageName + ".fileprovider", file)
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } else {
            uri = Uri.fromFile(file)
        }
        return uri
    }

}