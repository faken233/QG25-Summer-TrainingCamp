#  **第三章Activity第四章控件**

1.Toast最后记得加show（），否则点击按钮没有反应

### Button

ViewBinding的使用

​     先在 [build.gradle.kts](D:\AndroidStudioProjects_Kotlin\app\build.gradle.kts) （Module：app）中声明：


```kotlin
android{
    ...
    buildFeatures{
viewBinding=true
    }
    ...
}

//在MainActivity中
private lateinit var binding: ActivityMainBinding//如果要在onCreate外操控控件，将binding设置为全局变量
onCreate（）{
binding= ActivityMainBinding.inflate(layoutInflater)//借助布局文件对应的Binding去加载布局
    setContentView(binding.root)//传入根元素的实例成功加载布局内容
        binding.button.setOnClickListener{//获取控件示例并且进行设置
            Toast.makeText(this,"You Click Button",Toast.LENGTH_SHORT).show()
        }
}
```

注意，Kotlin声明的变量都必须在声明的同时对其进行初始化。而这里我们显然无法在声明全局binding变量的同时对它进行初始化，所以这里又使用了**lateinit**关键字对binding变量进行了延迟初始化。

### Menu

​	语法糖：设置和读取对象的字段时，可以直接.字段名：

```kotlin
val book=Book()
book.pages=500
bookPages=book.pages//在背后会自动转换成调用set和get方法
```

1.先创建一个文件夹menu，再在下面建一个menu文件叫main

```kotlin
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <item
        android:id="@+id/add_item"
        android:title="Add" />
    <item
        android:id="@+id/remove_item"
        android:title="Remove" />
</menu>
```

2.由于theme是noActionbar，设置Toolbar代替Action

```kotlin
val binding= ActivityMainBinding.inflate(layoutInflater)
 binding.toolbar.title=""//设置标题为空，不然会是项目名 ，放在setSupportActionBar之前    
setSupportActionBar(binding.toolbar) 
```

3.重写onCreateOptionsMenu和onOptionsItemSelected

```kotlin
override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    // 使用menuInflater对象将R.menu.main菜单文件加载到menu中
    menuInflater.inflate(R.menu.main,menu)
    return true//true表示允许菜单显示
}

override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when(item.itemId){
        R.id.add_item->Toast.makeText(this,"You Click add",Toast.LENGTH_SHORT).show()
        R.id.remove_item->Toast.makeText(this,"You Click remove",Toast.LENGTH_SHORT).show()
    }
    return true
}
```

### Intent

显式Intent：

```kotlin
val intent= Intent(this,SecondActivity::class.java)//这里的SecondActivity::class.java类似于java中的SecondActivity.class
   startActivity(intent)
```

隐式Intent：找到指定的action和catagory对应的活动启动：

```kotlin
//在AndroidManifest.XML中
<intent-filter>
    <action android:name="com.example.activitytest.ACTION_START"/>
    <category android:name="android.intent.category.DEFAULT"/>//这是默认的category，会自动添加到intent中
</intent-filter>

val intent = Intent("com.example.activitytest.ACTION_START")
//action和category都要匹配上
//添加其他的category intent.addCategory("")
startActivity(intent)
```

 启动网页：

```
val intent = Intent(Intent.ACTION_VIEW)//ACTION_VIEW是系统内置动作
intent.data= Uri.parse("https://www.baidu.com")
startActivity(intent)
```

拨号：

```kotlin
val intent = Intent(Intent.ACTION_DIAL)
intent.data= Uri.parse("tel:10086")
startActivity(intent)
```

传递数据给下个活动：

```kotlin
val data="Hello SecondActivity"
val intent = Intent(this,SecondActivity::class.java)
intent.putExtra("extra_data",data)//第一个是键，第二个是要传的数据
startActivity(intent)

val data=intent.getStringExtra("extra_data")//接收
```

返回数据给上一个活动：

   1.用startActivityForResult()启动第二个活动
   2.在第二个活动中用键值对存放数据，用setResult向上一个活动返回数据
   3.在第一个活动中重写onActivityResult来得到返回的数据

```kotlin
val data="Hello SecondActivity"
val intent = Intent(this,SecondActivity::class.java)
startActivityForResult(intent,1)//第二个参数是请求码，只要是唯一值就行

//第二个活动中
val intent= Intent()
            intent.putExtra("data_return","Hello FirstActivity")
            setResult(RESULT_OK,intent)//第一个参数一般是RESULT_OK或者RESULT_CANCELED，第二个把带数据的intent传回去
            finish()
//重写onActivityResult
 override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){//先确定请求码
            1->if(resultCode==RESULT_OK){//再对setResult的第一个参数
                val returnData=data?.getStringExtra("data_return")//避免空指针异常
                Log.d("MainActivity","return data is $returnData")
            }
        }
    }
```

通过返回键来向上一个活动返回数据：重写onBackPress

```kotlin
override fun onBackPressed() {
    val intent= Intent()
    intent.putExtra("data_return","Hello FirstActivity")
    setResult(RESULT_OK,intent)
    Log.d("SecondActivity","onBackPressed is done")
    super.onBackPressed()//注意这个super要放在后面，因为如果先调用super，那么活动就会被结束，影响setResult的使用
}
```



## Activity的生命周期

1.Android是采用任务来管理Activity的，一个任务就是一组存放在栈里的Activiy的集合
2.Activity的状态：运行，暂停，停止，销毁

Activity的生存期：onCreate，onStart, onResume, onPause, onStop, onDestory ,onRestart

3.想让活动以对话框的形式弹出：

```kotlin
 <activity
        android:name=".DialogActivity"
        android:exported="false"
        android:theme="@style/Theme.AppCompat.Dialog">
</activity>
```

4.当系统内存不足，把onStop 的活动回收，这样我们返回这个活动时，不会执行onRestart，而是执行onCreate，我们可以重写onSaveInstanceState（），来保存被回收的活动的数据，然后在onCreate中重新获取数据

```kotlin
override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    val tempData="Something you just type"
    outState.putString("data_key",tempData)//也是利用键值对存储数据
}

//

onCreate（savedInstanceState: Bundle?）{
    if(savedInstanceState!=null){
            val tempData=savedInstanceState.getString("data_key")
            Log.d("tag","tempData is $tempData")
        }else{
            Log.d("tag","no data")
        }
}
```

## **3.5 Activity的启动模式**

四种启动模式：standard，singleTop，singleTask，singleInstance

在AndroidManifest .XML中：android:launchMode="...." 进行修改

**standard**：默认的启动模式，不管是否存在都创建新活动，并且置于栈顶

**singleTop**：如果存在该Activity并且位于栈顶，直接使用，但如果存在但不位于栈顶，还是会创建新的实例

**singleTask**：检查是否存在该Activity的实例，如果存在则直接使用，并且在他之上的其他Activity全部出栈

singleInstance：创建新的返回栈来管理这个Activity，以实现多个程序共享一个activity实例的效果



## **3.6 Activity的最佳实践**

**知晓当前是在界面是在哪个Activity中：**
   1.先创建一个普通的Kotlin类：BaseActivity，继承于AppCompatActivity，重写onCreate方法

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Log.d("BaseActivity",javaClass.simpleName)//javaClass表示获取当前实例的class对象， .simpleName是获取当前实例的类名
}
```

  2.然后让所有的Activity去继承BaseActivity，这样我们在点击按钮等操作时，就可以直接在日志中看到该界面是在哪个活动中



**随时随地退出程序**
    1.创建一个单例类ActivityCollector（确保所有的Activity都被放在同一个集合）作为Activity 的集合

```kotlin
object ActivityCollector {
    private val activities=ArrayList<Activity>()
    fun addActivity(activity:Activity){
        activities.add(activity)
    }
    fun removeActivity(activity:Activity){
        activities.remove(activity)
    }
    fun finishAll(){
        for(activity in activities){
         if(!activity.isFinishing) {
             activity.finish()
         }
        }
        activities.clear()
    }
}
```

   2.在BaseActivity中把当前活动添加到集合中，并且重写onDestroy方法，把要销毁的活动从集合中移除

```kotlin
open class BaseActivity : AppCompatActivity (){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("BaseActivity",javaClass.simpleName)
        ActivityCollector.addActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }

}
```

   3.在需要直接退出的地方调用ActivityCollector.finishAll



**方便别人知道启动我的Activity需要传递哪些数据**

```kotlin
//在我的SecondActivity中写
companion object {//在companion object中的方法类似于java中的静态方法
    fun actionStart(context:Context,data1:String,data2:String){
        val intent = Intent(context,NormalActivity::class.java)
        intent.putExtra("param1",data1)
        intent.putExtra("param2",data2)
        context.startActivity(intent)
    }
}

//这样别人在启动我的活动时只需要
SecondActivity.actionStart(this,"data1","data2")
```

## **3.7 Kotlin标准函数和静态方法**

### 标准函数 with，run和apply

标准函数：Standard.kt文件中定义的函数

**with**：第一个参数是任意类型的对象，第二个参数是lambda表达式，他的上下文是第一个参数，lambda表达式的最后一行代码就是with函数的返回值

```kotlin
fun main(){
    val list=listOf("apple","orange","grape","pear")
    val result=with(StringBuilder()){
        append("Start eating\n")
        for(fruit in list){
         append(fruit).append("\n")
        }
        append("Ate all fruit")
        toString()
    }
    println(result)
}
```

**run**：和with很像，只有一个lambda参数，要用对象来调用，相当于把with的第一个参数拿出去，返回值也是lambda的最后一行

```kotlin
fun main(){
    val list=listOf("apple","orange","grape","pear")
    val result= StringBuilder().run{
        append("Start eating\n")
        for(fruit in list){
            append(fruit).append("\n")
        }
        append("Ate all fruit")
        toString()
    }
    println(result)
}
```

**apply**：和run很像，但是返回值只能是调用对象本身

```kotlin
fun main(){
    val list=listOf("apple","orange","grape","pear")
    val result= StringBuilder().apply{
        append("Start eating\n")
        for(fruit in list){
            append(fruit).append("\n")
        }
        append("Ate all fruit")
    }
    println(result.toString())//apply只能返回一个StringBuilder的对象，也就是说result是一个stringBuilder对象
}
```

### 静态方法

静态方法：指的是那些不用创建对象就可以调用的方法
**object单例类**，类中所有的方法都可以用类似于静态方法的方式调用，类名+方法名，虽然这里的方法还不是静态方法

**companion object{}**：当我们只想在普通类中把某个方法变成可以类似静态方法调用，我们可以把方法放在companion object{}中，但是这个方法还不是静态方法，而是companion object在类中创建了一个伴生类，until.doAction() 就是用伴生对象去调用其中的方法

真正的静态方法：

1.**@JvmStatic**：在单例类或者companion object的方法上添加注解：**@JvmStatic**

2.**顶层方法**：指没有定义在类中的方法，在Kotlin可以直接调用方法名，在java中需要文件名Kt.方法名调用

## **第四章 控件**

1.android：gravity 指定文字的对齐方式，包括：top，bottom，end，start，可以用 | 指定多个值

2.指定控件的宽度单位是 dp

3.指定文字大小单位：sp

4.按钮内容自动转大写：android：textAllCaps="true"

5.给EditText指定最大行数，当输入的内容超过两行时，文本就会向上滚动，而不会拉伸控件的高度
                  **android:maxLines="2"**

6.直接给ImageView设置图片：**android:src="@drawable/ic_launcher_foreground"**
   在代码中设置：binding.imageView.**setImageResource**(R.drawable.ic_launcher_background)

7.控件的可见性：visible：默认可见  
                               invisible：不可见但是还占据位置，可以理解成透明
                               gone：不可见也不占据位置
在XML中，**android:visibility= "gone"**

8.**进度条progressBar**

可以在代码中设定  ：binding.progressBar.visibility=View.GONE（注意是View.GONE,大写）
设置进度条的样式：

```kotlin
style="?android:attr/progressBarStyleHorizontal"//注意这前面没有android：
android:max="100"//设置进度最大值
//在代码中动态修改进度
binding.progressBar.progress=binding.progressBar.progress+10
```

9.对话框AlertDialog

```kotlin
AlertDialog.Builder(this).apply{//使用apply标准函数
    setTitle("This is Dialog")
    setMessage("Something Important")
    setCancelable(false)//设置是否可以用返回键关闭对话框
    setPositiveButton("OK"){dialog,which->}//两个参数，第二个参数是lambda回调函数，处理被点击的逻辑
    setNegativeButton("Cancel"){dialog,which->}
    show()//记得最后显示
}
```

## **布局**

1.layout_gravity: 指定控件在布局中的对齐方式
需要注意的是：当控件水平排列时，只有竖直方向上的对齐会生效

2.layout_weight:根据比例分配控件所占的空间，水平布局的情况下，一般会把控件的宽度指定为0dp

### 引入布局

1.先在layout文件下创建新的布局文件，命名为title（注意被引入布局的宽高要设置成好，不然会覆盖主布局）

2.引入布局：<include layout="@layout/title"/>



![image-20250710211833180](C:\Users\yyz20\AppData\Roaming\Typora\typora-user-images\image-20250710211833180.png)

## **4.5 ListView**

内置的子项布局：android.R.layout.simple_list_item_1

定制自己的ListView：

1.定义一个实体类Fruit作为适配器的适配类型

```kotlin
class Fruit(val name:String,val imageId:Int) {}
```

2.创建子项布局（注意：子项的height不能是match_parent）

3.创建自定义的适配器类FruitAdapter继承于ArrayAdapter

```kotlin
class FruitAdapter(activity: Activity,val resourceId:Int,data:List<Fruit>):ArrayAdapter<Fruit>(activity,resourceId,data) {
//重写getView
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
         
        val view:View//优化子项会多次创建布局的问题
         val viewHolder:ViewHolder//优化多次findViewByID获取控件实例的问题
        if(convertView==null) {
            //convertView就是用来缓存加载好的布局
            view = LayoutInflater.from(context).inflate(resourceId, parent, false)
            val fruitName: TextView =view.findViewById(R.id.fruitName)
            val fruitImage: ImageView =view.findViewById(R.id.fruitImage)
            viewHolder=ViewHolder(fruitName,fruitImage)
            view.tag=viewHolder//将ViewHolder的对象存在View中
        }else{
            view=convertView
            viewHolder=view.tag as ViewHolder//重新取出viewHolder
        }

        val fruit=getItem(position)//获取当前项的Fruit实例
        if(fruit!=null){
            viewHolder.fruitImage.setImageResource(fruit.imageId)
            viewHolder.fruitName.text=fruit.name
        }
        return view
    }
    inner class ViewHolder(val fruitName:TextView,val fruitImage:ImageView)//创建内部类ViewHolder
}
```

4.在MainActivity中初始化数组和适配器，把适配器传给listView

```kotlin
class MainActivity : AppCompatActivity() {
    private val fruitList=ArrayList<Fruit>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initFruit()//初始化水果数据
        val adapter=FruitAdapter(this,R.layout.fruit_item,fruitList)
        binding.listView.adapter=adapter
        //给子项设置点击事件
        binding.listView.setOnItemClickListener{parent,view,position,id->
            val fruit=fruitList[position]//判断点击的哪个子项
            Toast.makeText(this,fruit.name,Toast.LENGTH_SHORT).show()
        }
    }
    private fun initFruit(){
        repeat(2){//repeat是一个标准函数，意思是把后面的代码执行n遍
            fruitList.add(Fruit("Apple",R.drawable.apple_pic))
            fruitList.add(Fruit("Banana",R.drawable.banana_pic))
            fruitList.add(Fruit("Orange",R.drawable.orange_pic))
            fruitList.add(Fruit("Watermelon",R.drawable.watermelon_pic))
            fruitList.add(Fruit("Pear",R.drawable.pear_pic))
            fruitList.add(Fruit("Grape",R.drawable.grape_pic))
            fruitList.add(Fruit("Pineapple",R.drawable.pineapple_pic))
            fruitList.add(Fruit("Strawberry",R.drawable.strawberry_pic))
            fruitList.add(Fruit("Cherry",R.drawable.cherry_pic))
            fruitList.add(Fruit("Mango",R.drawable.mango_pic))
        }
    }
}
```

## **4.6 RecyclerView**

1. 添加依赖
   implementation("androidx.recyclerview:recyclerview:1.4.0")

   注意在主布局添加RecyclerView时，因为RecyclerView不是内置在系统SDK中，则要写出完整的包路径

   ```kotlin
   <androidx.recyclerview.widget.RecyclerView
       android:id="@+id/recyclerView"
       android:layout_width="match_parent"
       android:layout_height="match_parent"
       />
   ```

2.创建子项布局（注意子项高度）

3.为适配器设置实体类Fruit

```
class Fruit(val name:String,val ImageId:Int) {}
```

4.创建适配器类FruitAdapter：继承于RecyclerView.Adapter<FruitAdapter.ViewHolder>

```kotlin
class FruitAdapter(val fruitList: List<Fruit>) : RecyclerView.Adapter<FruitAdapter.ViewHolder>() {
  
    //先写一个内部类ViewHolder
    inner class ViewHolder(binding: FruitItemBinding) : RecyclerView.ViewHolder(binding.root) {
        //因为RecyclerView.ViewHolder只接收View对象，所以这里有用binding.root
        
        val fruitName= binding.fruitName
        val fruitImage = binding.fruitImage
    }

//重写onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //加载子项布局
        val binding = FruitItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)
        return ViewHolder(binding)
    }

    //重写onBindViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fruit = fruitList[position]
        //给最外层子项设置点击事件
        holder.itemView.setOnClickListener {
            val fruit = fruitList[position]
            Toast.makeText(holder.itemView.context, "you click view ${fruit.name}", Toast.LENGTH_SHORT).show()
        }
        //给子项图片设置点击事件
     holder.fruitImage.setOnClickListener {
            val fruit = fruitList[position]
            Toast.makeText(holder.itemView.context, "You click image ${fruit.name}", Toast.LENGTH_SHORT).show()
        }
        //
        
        holder.fruitName.text = fruit.name
        holder.fruitImage.setImageResource(fruit.ImageId)
    }
//重写getItemCount
    override fun getItemCount() = fruitList.size

}
```

5.创建布局管理器，创建适配器

```kotlin
initFruit()
val binding = ActivityMainBinding.inflate(layoutInflater)
setContentView(binding.root)
val layoutManager= LinearLayoutManager(this)
binding.recyclerView.layoutManager=layoutManager
val adapter=FruitAdapter(fruitList)
binding.recyclerView.adapter=adapter
```

## **4.8 Kotlin课堂延迟初始化和密封类**

### 延迟初始化

private lateinit var adapter :MsgAdapter
但是要记得初始化，否则程序还是会崩溃，可以加一个初始化判断 ：：adapter.isInitialized

### 密封类

**密封类（Sealed Class）** 是一种特殊的类，用于表示受限的类层次结构。它提供了一种方式，可以将一组相关的类组织在一起，并限制这些类的继承关系，从而提高代码的安全性和可维护性。
**结合 `when` 表达式**实现模式匹配，提高代码的可读性和安全性

```kotlin
//如果Result是接口，下面的when就必须要加上else否则编译不通过
interface Result
class Success(val msg:String):Result
class Failure(val error:Exception):Result
fun getMessageResult(result: Result)=when(result){
    is Success->result.msg
    is Failure->"Error is ${result.error.message}"
    else->throw IllegalArgumentException()
}

//把Result改为密封类，继承类就要加上括号
sealed class Result
class Success(val msg:String):Result()
class Failure(val error:Exception):Result()
fun getMessageResult(result: Result)=when(result){
    is Success->result.msg
    is Failure->"Error is ${result.error.message}"
    //此时不用else也可以，当我有一个Unknown类继承Result时
    //就要再写一个is Unknown->...........
}

```
