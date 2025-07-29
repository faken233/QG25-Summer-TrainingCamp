# Retrofit

发送网络请求的方式：

​     HttpURLConnection
​     OkHttp

解析Json：用Gson,JSONObject

解析XML：Pull，Sax

Retrofit允许对功能同属一类的服务器接口定义到同一个接口文件中

1.添加依赖：

```kotlin
implementation("com.squareup.retrofit2:retrofit:2.6.1")//自动下载了Retrofit，okHttp和Okio，Okio是OkHttp的通信基础
implementation("com.squareup.retrofit2:converter-gson:2.6.1")//自动下载了GSON库
```

2. 声明网络权限，添加网络安全配置

   ```kotlin
   
   <uses-permission android:name="android.permission.INTERNET"/>
    <application
         android:networkSecurityConfig="@xml/network_config"//允许明文流量传输
   //或者直接android:usescleartextTraffic="true"
    >
   //在xml下创建xml资源文件
   <?xml version="1.0" encoding="utf-8"?>
   
     <network-security-config>
         <base-config cleartextTrafficPermitted="true">
             <trust-anchors>
                 <certificates src="system"/>
             </trust-anchors>
         </base-config>
     </network-security-config>
   ```

3. 创建要接收的对象类

   ```kotlin
   class App(val id:String,val name:String,val version:String)
   ```

4. 创建接口文件，按功能+Service命名

   ```kotlin
   interface AppService {
       @GET ("get_data.json")//用@GET注解传入相对路径
       fun getAppData(): Call<List<App>>//返回值必须声明成Retrofit内置的Call类型，并且用泛型指定把服务器返回的数据转换成什么对象
   }
   ```

5. 发送请求

```kotlin
val button =binding.getAppDataBtn
button.setOnClickListener{
    //构建一个Retrofit对象
    val retrofit= Retrofit.Builder()
        .baseUrl("http://10.0.2.2/")//必需，指定所有Retrofit请求的根路径，这里不要用localhost
        .addConverterFactory(GsonConverterFactory.create())//指定Retrofit在解析数据时使用的转换库
        .build()
    val appService=retrofit.create(AppService::class.java)//创建接口的动态代理对象，这样才能调用接口的方法
    //调用getAppData返回一个Call<List<App>>对象，然后调用enqueue去进行网络请求
    //自动回调数据切换线程，重写Callback的onResponse和onFailure
    appService.getAppData().enqueue(object: Callback<List<App>>{
        override fun onResponse(call: Call<List<App>>, response: Response<List<App>>) {
            val list=response.body()
            if(list!=null) {
                for (app in list) {

              Log.d("MainActivity","id is ${app.id}")
              Log.d("MainActivity","name is ${app.name}")
              Log.d("MainActivity","version is ${app.version}")

                }
            }
        }

        override fun onFailure(call: Call<List<App>>, t: Throwable) {
            t.printStackTrace()//打印异常信息
        }
    })
}
```

### 处理复杂的接口地址类型



```kotlin

//对于接口：GET http://example.com/<page>/get_java.json
interface ExampleService {
    @GET ("{page}/get_data.json")//使用{page}占位符
    fun getData(@Path("page")page:Int): Call<Data>//用@Path（“page”）来声明参数，在调用getData时自动把参数传入占位符
}
```

对于带一系列参数的服务器接口

```kotlin
hhtp：//example.com/get_data.json?u=<user>&t=<token>
// 用？连接参数部分，每个参数都是用等号连接的键值对，参数之间用&隔开
interface ExampleService {
    @GET ("get_data.json")
    fun getData(@Query("u")user:String,@Query("t")token:String):Call<Data>
}
```

常见的HTTP请求：
GET，POST，PUT（ 修改），PATCH（修改），DELETE

```kotlin
删除数据
DELETE http://example.com/data/<id>

interface ExampleService {
   @DELETE("data/{id}")
   fun deleteData(@Path("id")id:String):Call<ResponseBody>
    //使用ResponseBody作为泛型是因为：删除数据和获取数据不同，对于服务器响应的数据不关心，ResponseBody表示Retrofit可以接收任意类型的响应数据，并且不进行解析
}
```



提交数据

```kotlin
POST http：//example/data/create

interface ExampleService {
  @POST("data/create")
  fun createData(@Body data:Data):Call<ResponseBody>//发送请求时把data转换为Json文本然后放到HTTP的body中，服务器接收到数据后，只需要解析出来即可
}
```



在HTTP请求的header中指定参数：

```kotlin
GET http://example.com/get_data.json
User-Agent:okhttp
Cache-Control:max-age=0


interface ExampleService {
  @Headers ("User-Agent:okhttp","Cache-Control:max-age=0")// 静态设置header参数
  @GET("get_data.json")
  fun getData():Call<Data>
}

//动态指定header
interface ExampleService {
  @GET("get_data.json")
  fun getData(@Header（“user -Agent”）userAgent：String，
              @Header（“Cache-Control”）cacheControl：String):Call<Data>
}
```



每次都要去创建动态代理对象太麻烦，我们可以把前面的代码封装起来，根据不同的需要传入相应的Class

```kotlin
object ServiceCreator {
    private  const val BASE_URL="http://10.0.2.2"
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    fun<T>create(serviceClass:Class<T>):T=retrofit.create(serviceClass)
}
```

