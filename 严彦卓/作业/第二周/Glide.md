# Glide

![image-20250717171153091](C:\Users\yyz20\AppData\Roaming\Typora\typora-user-images\image-20250717171153091.png)

Glide和Picasso比较简单好用

注意：binding要声明为全局变量时，不能直接在onCreate外进行初始化，因为此时Acitivity还未被附加到窗口上，会出现空指针异常；同理，Button等控件的实例也不能在onCreate外初始化，因为binding还没初始化，从binding拿不到控件实例，同样会出现空指针异常

**基础用法：**

​	1.添加依赖

```kotlin
dependencies{
implementation(libs.glide)
...
}
```

​	2.加载图片文件

```kotlin
 button=binding.btn
        image=binding.image

        button.setOnClickListener {
            load()
        }

    }

    fun load(){
        val url:String="https://cn.bing.com/az/hprichbg/rb/Dongdaemun_ZH-CN10736487148_1920x1080.jpg"
        Glide.with(this).load(url).into(image)
        //调用Glide.with()方法用于创建一个加载图片的实例。with()方法可以接收Context、Activity或者Fragment类型的参数，如果调用的地方不在Activity也不在Fragment中，则可以获取当前应用程序的ApplicationContext
        //with()方法中传入的实例会决定Glide加载图片的生命周期，如果传入的是Activity或者Fragment的实例，那么当这个Activity或Fragment被销毁的时候，图片加载也会停止。如果传入的是ApplicationContext，那么只有当应用程序被杀掉的时候，图片加载才会停止。
        
        //load方法还可以加载各种资源：网络图片、本地图片、应用资源、二进制流、Uri对象
        // 加载本地图片File file = getImagePath();
// 加载应用资源
int resource = R.drawable.image;
Glide.with(this).load(resource).into(imageView);

// 加载二进制流
byte[] image = getImageBytes();
Glide.with(this).load(image).into(imageView);

// 加载Uri对象
Uri imageUri = getImageUri();
Glide.with(this).load(imageUri).into(imageView);
    }
}
```



**占位图：**Glide的绝大多数拓展功能都是在with和into之间添加功能

​		**加载占位图：placeholder（）**

​			由于加载网络图片需要时间，所以可以放一张加载占位图提示用户图片正在加载

​		**异常占位图：error（）**加载失败显示的图片

```kotlin
fun load(){
    val url:String="https://cn.bing.com/az/hprichbg/rb/Dongdaemun_ZH-CN10736487148_1920x1080.jpg"
    Glide.with(this).load(url)
        .placeholder(R.drawable.da702e8007e4bc47ee9cc321aa03070)//直接传入图片
        .diskCacheStrategy(DiskCacheStrategy.NONE)//由于Glide会把先前获取过的图片缓存，下次加载就直接在缓存中获取，占位图可能来不及出现，这里禁用了Glide的缓存功能
     .error(R.drawable.error)//直接传入加载失败的图片
        .into(image)
}
```



**指定加载图片格式：**

​	Glide支持加载动态GIF图，而picasso不可以，Glide会自动判断图片格式，不需要另外编写代码

​	指定只能加载静态图片 **.asBitmap()**：静态图正常显示，动态图->显示第一帧
​	指定只能加载动态图片 **.asGif()**: 静态图- >加载失败，动态图->正常显示



**指定图片大小：**事实上，Glide会根据ImageView的大小决定图片的大小，不会直接加载完整的图片

如果一定要指定图片大小：加上**.override(100, 100)**

**Glide的缓存机制**：内存缓存，硬盘缓存
	内存缓存：防止应用重复将图片数据读取到内存当中
	硬盘缓存：防止应用重复从网络或其他地方重复下载和读取数据。