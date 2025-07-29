# Glide

## 从一个URL加载图片（动图）

```kotlin
val url = "https://img8.bitautoimg.com/usercenter/dzusergroupfiles/2022/08/15/w800_yichecar_5b549cb62c19480594038d2aefcd27f0.jpg.webp"
Glide
    .with(this)
    .load(url)
    .into(binding.imageView)
```

用Glide完成一个完整的图片加载功能请求，需要向其构造器中传入至少3个参数，分别是：

**with（context： Context）**：可以传递一个Activity或Fragment对象

**load（imageUrl： String）**：传入要加载的图片的URL

**into（imageView）**：将解析的图片传入的目标imageView

## 从Res资源中加载图片

```kotlin
val resourceId = R.mipmap.ic_launcher_round
Glide
    .with(this)
    .load(resourceId)
    .into(binding.imageView)
```

## 从文件中加载图片                                                                              

```kotlin
val file = File(
    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
    "poster.png"
);
Glide
    .with(this)
    .load(file)
    .into(binding.imageView)
```

这样写可以从手机的本地文件中加载图片，Environment.DIRECTORY_PICTURES是本地图片目录、Environment.DIRECTORY_MUSIC是本地音乐目录、Environment.DIRECTORY_DOWNLOADS是本地下载目录...

注意在使用从本地文件中加载图片之前要在AndroidManifesi.xml中添加权限

```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
```

## 从Uri中加载图片

Uri是“统一资源标识符”，它的范围比URL更广，可以从内容提供者（Content Provider）、媒体库（MediaStore）、云端（网络图片中加载

```kotlin
val uri = resourceIdToUri(this, R.mipmap.ic_launcher_round)
Glide.with(this).load(uri).into(binding.imageView)
...
fun resourceIdToUri(context: Context, resourceId: Int): Uri {
        return "android.resource://${context.packageName}/$resourceId".toUri()
}
```

上面的代码创建了一个方法用于返回Uri给Glide

- 内容提供者：content://media/external/images/media/12345

- 媒体库：content://media/external/images/media/12345

- 云端：https://example.com/image.png

- 本地文件：file:///storage/emulated/0/Pictures/poster.png

- FileProvider：content://com.example.fileprovider/my_private_image.jpg

## 占位图

当网络环境不好时，加载的图片有时需要一会儿才能加载出来有一个占位图预先显示在界面上会让整个过程显得更为合理。

```kotlin
Glide
	.with(...)
	.load(...)
	.placeholder(...)	//占位图，传入一个图片资源
	.into(...)
```

占位图一定不能是一个网络的url，这样占位图和目标加载图都无法显示出来，因此一般使用App内的资源和图片
（比如mipmap里的logo）

## 出错占位图

假设要从网页加载一张图片，但恰好此时网络不可用，Glide会给出我们选项去进行出错的回调，并采取合适的行动。

```kotlin
Glide
	.with(...)
	.load(...)
	.placeholder(...)
	.error(...)		//网络请求超时或其他问题时会展示出错占位图
	.into(...)
```

## **crossFade**

crossFade动画可以让图片的出现变得更加平滑

```kotlin
Glide
	.with(...)
	.load(...)
	.transition(DrawableTransitionOptions.withCrossFade(...))	//方法内可以传入一个以毫秒为单位的出现时间，默认为300毫秒
	.into(...)
```

## dontAnimate

dontAnimate用于直接显示图片而不需要crossfade效果

```Kotlin
Glide
	.with(...)
	.load(...)
	.dontAnimate()
	.into(...)
```

## resize(x, y)调整图片大小

Glide可以调用override()方法在ImageView之前调整图片的大小

```kotlin
Glide
	.with(...)
	.load(...)
	.override(x, y) //调整图片大小
	.into(...)
```

## 缩放图片

**certerCrop**

centerCrop()方法会缩放图片让图片充满整个ImageView的边框，然后裁掉超出的部分。ImageView会被完全填充满，但是图片可能不能完全显示出来

```kotlin
Glide
	.with(...)
	.load(...)
	.centerCrop() 
	.into(...)
```

**fitCenter**

fitCenter()方法会让图片两边都相等或小于ImageView的所要求的边框。图片会被完整显示出来但不能完全填充整个ImageView

```kotlin
Glide
	.with(...)
	.load(...)
	.fitCenter
	.into(...)
```

## 将Gif当作Bitmap播放

asBitmap()方法可以只显示动图的第一帧，放在with()方法后面

```kotlin
Glide
	.with(...)
	.asBitmap()
	.load(...)
	.into(...)
```

## 内存/磁盘缓存

Glide中有两个方法可以对图片在内存和缓存方面做出影响，分别是skipMemoryCache()和diskCacheStrategy()

前者参数为true时会告诉Glide跳过内存缓存，Glide就不会把这个图片缓存到内存里；后者参数为DiskCacheStrategy.NONE时会告诉Glide禁止磁盘缓存，但不会禁用内存缓存。两个方法同时调用可以一起禁用

```kotlin
Glide
	.with(...)
	.load(...)
	.diskCacheStrategy(DiskCacheStrategy.NONE)
	.skipMemoryCache(true)
	.into(...)
```

其实diskCacheStrategy()方法中的参数是一个枚举类型的参数，可以传入一下的参数：

DiskCacheStrategy.NONE 啥也不缓存

DiskCacheStrategy.SOURCE 只缓存全尺寸图

DiskCacheStrategy.RESULT 只缓存最终降低分辨后用到的图片

DiskCacheStrategy.ALL 缓存所有类型的图片（默认）

## 缩略图

缩略图不同于前面的占位图，缩略图是一个动态的占位图，可以从网络加载。缩略图也会被先加载，知道实际图片请求加载完毕。如果缩略图获得的时间晚于原始图片，它并不会替代原始图片，而是简单地被忽略掉。

```kotlin
Glide
	.with(...)
	.load(...)
	.thumbnail( 0.1f ) 	//加载原图大小10%的图片
	.into(...)
```

