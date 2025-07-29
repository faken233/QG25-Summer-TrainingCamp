# Jetpack

Jetpack是一个开发组件工具集，这些组件通常定义在AndroidX中，具有较好的向下兼容性

Jetpack主要是由：基础，架构，行为，界面四部分组成

## 13.2 ViewModel

ViewModel的生命周期和Activity不同，专门用来存放和页面相关的数据，手机屏幕旋转时，Activity会重新创建，而ViewModel不会，只有Activity退出时，ViewModel才会销毁

binding的初始化应该在onCreate中进行，否则layoutInflater可能还没有被初始化

**简单用法：**

​    1.添加依赖

```
implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
```

​    2.给每一个Activity或者Fragment都创建对应的ViewModel并继承于ViewModel，添加需要的存储的数据变量

```
class MainViewModel : ViewModel() {
    var counter=0
}
```

​    3.在活动中通过ViewModelProvider创建ViewModel的实例 

```kotlin
class MainActivity : AppCompatActivity() {
    lateinit var viewModel:MainViewModel
    lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewModel=ViewModelProvider(this).get(MainViewModel::class.java)
        binding.plusOneBtn.setOnClickListener{
            viewModel.counter++
            refreshCounter()
        }
        refreshCounter()
    }
    private fun refreshCounter(){
      binding.infoText.text=viewModel.counter.toString()
    }
}
```



**向ViewModel传递参数：**

1.修改MainViewModel

```Kotlin
class MainViewModel(countReserved:Int) : ViewModel() {

    var counter=countReserved
}
```

2.新建MainViewModelProvider继承ViewModelProvider.Factory向MainViewModel传递参数

```kotlin
class MainViewModelFactory(private val countReserved:Int): ViewModelProvider.Factory {
    override fun <T: ViewModel>create (modelClass:Class<T>):T{
        return MainViewModel(countReserved) as T
    }
}
```

```kotlin
class MainActivity : AppCompatActivity() {
    lateinit var viewModel:MainViewModel
    lateinit var binding:ActivityMainBinding
    lateinit var sp:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        sp=getPreferences(Context.MODE_PRIVATE)//初始化sp
        val countReserved=sp.getInt("count_reserved",0)//第一个参数是键，第二个参数表示如果没有读到就返回默认值0
        viewModel=ViewModelProvider(this,MainViewModelFactory(countReserved))
            .get(MainViewModel::class.java)
        binding.plusOneBtn.setOnClickListener{
            viewModel.counter++
            refreshCounter()
        }
        binding.clearBtn.setOnClickListener {
            viewModel.counter=0
            refreshCounter()
        }
        refreshCounter()
    }
//退出程序的时候不会清零
    override fun onPause() {
        super.onPause()
        sp.edit {
            putInt("count_reserved",viewModel.counter)
        }
    }
```

## 13.3 Lifecycles

在非Activity的类中感知Activity的生命周期，可以通过手写监听器，重写Activity相应的生命周期方法，但是比较麻烦

1.新建MyObserver类继承于LifecycleObserve

```kotlin
class MyObserver:LifecycleObserver {
    @OnLifecycleEvent((Lifecycle.Event.ON_START))//对应七个生命周期，还有ON_ANY对应任意生命周期回调
    
    fun activityStart(){
        Log.d("MyObserver","activityStart")
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun activityStop(){
        Log.d("MyObserver","activityStop")
    }
}
```

2.在MyObserver用注解+定义方法感知生命周期

3.在活动中为生命周期添加观察者\

```kotlin
lifecycle.addObserver(MyObserver())
```



## 13.4 LiveData

？：   表达式不为空就返回左边，为空返回右边

前面是手动去更新界面，如果在ViewModel中有子线程耗时逻辑，那么点击后更新的数据可能不变

绝大多数情况下，LiveData和ViewModel结合使用，可以包装任何类型的数据，然后在Activity中去观察这个数据，如果发生变化，就可以主动把数据变化通知给Activity

```kotlin
class MainViewModel(countReserved:Int) : ViewModel() {

    var counter=MutableLiveData<Int>()//把counter修改为MutabelLiveData对象，这是一种可变的LiveData，他有三种用法：getValue（），setValue（）（只能在主线程中调用），postValue（）（在非主线程中调用给LiveData设置数据）
    init{
        counter.value=countReserved
    }
    fun plusOne(){
        val count=counter.value?:0
        counter.value=count+1
    }
    fun clear(){
        counter.value=0
    }
}
```

```kotlin
class MainActivity : AppCompatActivity() {
    lateinit var viewModel:MainViewModel
    lateinit var binding:ActivityMainBinding
    lateinit var sp:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
       
        sp=getPreferences(Context.MODE_PRIVATE)
        val countReserved=sp.getInt("count_reserved",0)
        viewModel=ViewModelProvider(this,MainViewModelFactory(countReserved))
            .get(MainViewModel::class.java)
        binding.plusOneBtn.setOnClickListener{
           viewModel.plusOne()
        }
        binding.clearBtn.setOnClickListener {
            viewModel.clear()
        }
        //observe观察数据变化，第一个参数是LifecycleOwner，继承了AppCompatActivity的Activity，或者是继承自androidx .fragment.app.Fragment的Fragment都是LifecycleOwner，可以直接传this，第二个参数是Observer接口，counter数据发生变化就会回调到这里，在这进行界面更新
       viewModel.counter.observe(this, Observer{
           count->binding.infoText.text=count.toString()
       })

    }

    override fun onPause() {
        super.onPause()
        sp.edit {
            putInt("count_reserved",viewModel.counter.value?:0)
        }
    }
```

但是这样会把counter这个可变的LiveData暴露给外部，可能被设置修改数据，

```kotlin
class MainViewModel(countReserved:Int) : ViewModel() {
val counter: LiveData<Int>//把counter声明为不可变的LiveData
    get()=_counter//给get设置返回_counter, 这样只能调用counter获得_counter,但是不能给counter设置数据
    
    private val _counter=MutableLiveData<Int>()//private，并且改名为 _counter
    init{
        _counter.value=countReserved
    }
    fun plusOne(){
        val count=_counter.value?:0
        _counter.value=count+1
    }
    fun clear(){
        _counter.value=0
    }
}
```

## map和switchMap

**map**方法用于将实际包含数据的LiveData和仅用于观察数据的LiveData进行转换：比如User类里包含name和age，如果现在要求只要显示name，不关心age，我们就可以用map进行转换

```kotlin
class MainViewModel(countReserved:Int) : ViewModel() {
    private val userLiveData = MutableLiveData<User>()
    val userName: LiveData<String> =
        userLiveData.map { user -> "${user.firstName} ${user.lastName}" }
//书中的Transformation已经被弃用，直接用LiveData 的map，直接传入转换函数
}
```



**switchMap**

 当ViewModel中的LiveData是调用另外的方法获取的，那我们就可以借助switchMap方法

```kotlin
viewModel.getUser(userId = "10")
    .observe(this, Observer { count1 -> binding.infoText.text = count1.toString() })
//  如果这样写那么每次调用getUser都会创建新的实例，但是observe和老的实例绑定所以会一直观察老的LiveData实例
```

 1.定义一个新的userIdLiveData观察userId

```kotlin
class MainViewModel(countReserved: Int) : ViewModel() {

private val userIdLiveData=MutableLiveData<String>()
    //调用userIdLiveData的switchMap将Repository中不可观察的LiveData转换为另一个可观察的LiveData
    val user:LiveData<User> =userIdLiveData.switchMap { userId1->Repository.getUser(userId1) }
    
    }


fun getUser(userId:String){
       userIdLiveData.value=userId
    }


object Repository {
    fun getUser(userId:String): LiveData<User> {
        val liveData= MutableLiveData<User>()
        liveData.value=User(userId,userId,0)
        return  liveData
    }
}
    
```

```kotlin

binding.getUserBtn.setOnClickListener {
    val  useId=(0..1000).random().toString()
    viewModel.getUser(useId)//当userIdLiveData的值发生变化，则观察他的switchMap就会执行，然后Repository.getUser()获取真正的用户数据，然后返回一个可观察的LiveData，在Activity中只需要观察这个就行了
}
viewModel.user.observe(this,Observer{
    user->binding.infoText.text=user.firstName
})
```

如果要获取ViewModel中某个数据的方法是没有参数的，那么我们可以创立一个空的LiveData，泛型指定为Any？

```
private val refreshLiveData=MutableLiveData<Any？>()
val refreshResult=refreshLiveData.switchMap{Repository.refresh()//假设这个方法存在
}
fun refresh（）{
refreshLiveData.value=refreshLiveData.value
}
```



## **13.5 Room**

ORM(Object Relational Mapping):也叫对象关系映射。

我们使用的是面向对象的编程语言，则我们使用的数据库是关系型数据库，将面向对象的语言和面向关系的数据库之间建立一种映射关系就是ORM

Room由三部分组成：Entity，Dao，Database

​	Entity：定义封装实际数据的实体类，每个实体类在数据库中都有对应的表，并且表中的列根据实体类的字段自动生成
​	Dao：对数据库的各项操作进行封装
​	Database：定义数据库中的关键信息，包括数据库的版本号，包含的实体类，提供Dao层的访问实例

1.添加插件和依赖

```kotlin
plugins {
 ...
id("kotlin-kapt")
}
dependencies {
	implementation("androidx.room:room-runtime:2.1.0")
    kapt("androidx.room:room-compiler:2.1.0")
...
}
```

2.给每个实体类添加id字段，并且声明为实体类

```kotlin
@Entity//注解声明为实体类
data class User(val firstName:String,val lastName:String,val age:Int){
@PrimaryKey(autoGenerate=true)//添加id字段，@PrimaryKey声明为主键，并且设置为自动生成
var id:Long=0
}
```

3.创建UserDao接口，声明为Dao

```kotlin
@Dao
interface UserDao {
@Insert
fun insertUser(user: User):Long//添加数据并且返回主键id

@Update
fun updateUser(newUser: User)

@Delete
fun deleteUser(user: User)

    //查找数据时或者以非实体类参数来进行增删改时，都必须要用@Query注解，可以动态检查SQL语法，如果有问题就会在编译时期报错
@Query("select * from User")
    fun loadAllUsers():List<User>

@Query("delete from User where lastName=:lastName")
fun deleteUserByLastName(lastName:String):Int
    
@Query("delete from User")//删除表中所有数据
    fun deleteAllUsers():Int//返回删除的行数
}
```

4. 定义database：数据库的版本号，包含的实体类，提供Dao层的访问实例

```kotlin
@Database(version=1, entities = [User::class])//如果有多个实体类就在class后面加逗号
//必须继承自RoomDatabase，并且声明为抽象类
abstract class AppDatabase:RoomDatabase(){
    abstract fun userDao():UserDao//提供抽象方法获取Dao实例
    
    //用单例模式获取AppDatabase的实例
    companion object{
        private var instance:AppDatabase?=null
@Synchronized
fun getDatabase(context: Context):AppDatabase{
    instance?.let{
        return it//不为空直接返回
    }
  //如果为空则利用Room的databaseBuilder构建实例，第一个参数不能用context，否则容易内存泄漏，
    //第二个参数是AppDatabse的Class类型，第三个参数是数据库名
    return Room.databaseBuilder(context.applicationContext,AppDatabase::class.java,"app_database")
    .build().apply { instance=this }
}

    }
}
```

测试

```kotlin
val userDao=AppDatabase.getDatabase(this).userDao()
val user1=User("Tom","Brady",40)
val user2=User("Tom","Hanks",63)
binding.addDataBtn.setOnClickListener {
    thread{
        user1.id=userDao.insertUser(user1)
        user2.id=userDao.insertUser(user2)
    }
}
binding.updateDataBtn.setOnClickListener {
    thread{
        user1.age=42
        userDao.updateUser(user1)
    }
    binding.deleteDataBtn.setOnClickListener {
        thread{
            userDao.deleteUserByLastName("Hanks")
        }
    }
    binding.queryDataBtn.setOnClickListener {
        thread{
            for(user in userDao.loadAllUsers()){
                Log.d("MainActivity",user.toString())
            }
        }
```

由于数据库操作都是属于耗时操作，，Room中默认不允许在主线程中进行数据库操作，所以要开启子线程

但也可以在创建AppDatabase实例的时候就加入allowMainThreadQueries（），建议只在测试环境下使用

```kotlin
return Room.databaseBuilder(context.applicationContext,AppDatabase::class.java,"app_database")
.allowMainThreadQueries（）
.build().apply { instance=this }
```

### **Room的数据库升级**

**新增表：**

1.创建新的实体类

2.创建对应的Dao接口

3.修改AppDatabase

​	1.修改版本号，添加class文件
​	2.增加获得对应Dao的抽象方法
​	3.实现Migration的匿名类，在其中添加升级逻辑

```kotlin
@Database(version=2, entities = [User::class,Book::class])//此处修改
abstract class AppDatabase: RoomDatabase(){
    abstract fun userDao():UserDao
    abstract fun bookDao():BookDao//此处修改



    companion object{
        ////此处修改
        val MIGRATION_1_2=object:Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("create table Book(id integer primary key autoincrement not null,name text not null,pages integer not null)")//注意建表语句要和实体类声明一致
                //这里Int对应Integer，String对应text
            }
        }
        private var instance:AppDatabase?=null
        @Synchronized
        fun getDatabase(context: Context):AppDatabase{
            instance?.let{
                return it
            }
            return Room.databaseBuilder(context.applicationContext,AppDatabase::class.java,"app_database").addMigrations(MIGRATION_1_2).build().apply { instance=this }
        }

    }
}
```



**新增列：**

1.增加实体类字段
2.修改AppDatabase

```kotlin
@Database(version=3, entities = [User::class,Book::class])//版本改为3
abstract class AppDatabase: RoomDatabase(){
    abstract fun userDao():UserDao
    abstract fun bookDao():BookDao


    companion object{
        val MIGRATION_2_3=object:Migration(2,3){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("alter table Book add column author text not null default 'unknown'")//default ‘unknown’表示默认为‘unknown’
            }
        }
        private var instance:AppDatabase?=null
        @Synchronized
        fun getDatabase(context: Context):AppDatabase{
            instance?.let{
                return it
            }
            return Room.databaseBuilder(context.applicationContext,AppDatabase::class.java,"app_database").addMigrations(MIGRATION_2_3).build().apply { instance=this }
        }

    }
}
```
