
# 文档






## 网络请求NetHelper

- `get` 发起get请求

```kotlin
fun <T> get(
            url: String, params: Map<String, String>?=null, 
            requestCode: Int = 0,
            callback: WrappedRequestCallback<T>.() -> Unit
)
```

示例：

```kotlin
NetHelper.get<String>("https://www.baidu.com/") {
    success { _, s ->
        println(s)
    }
    fail { _, e ->
        e.printStackTrace()
    }
} 
```

- `postJson` 以post方式传送Json格式数据

```kotlin
fun <reified T> postJson(
    url: String, model: Any? = null, requestCode: Int = 0,
    callback: WrappedRequestCallback<T>.() -> Unit
)
```

- `postJson` 以post方式传送Json格式数据（放在key为json_data中），并且带有文件

```kotlin
inline fun <reified T> postFile(
    url: String, model: Any? = null,
    requestCode: Int = 0, files: Array<File>,
    filePartName: String = "multipartFile",
    callback: WrappedRequestCallback<T>.() -> Unit
)
```


