# Material Design

Material Design是基于传统优秀的设计原则，结合丰富的创意和科学技术所开发的一套全新的界面设计语言，包含了视觉、运动、互动效果等特性

## 1.Toolbar

```xml
<androidx.appcompat.widget.Toolbar
    android:id="@+id/toolbar"
    android:layout_width="match_parent"	//宽度为match_parent
    android:layout_height="?attr/actionBarSize"	//高度与原先的actionBar一样
    android:background="?attr/colorPrimary"	//背景色设为colorPrimary
    android:theme="@style/ThemeOverlay.AppCompat.Dark.ActionBar"	//让Toolbar单独使用深色主题
    app:popupTheme="@style/ThemeOverlay.AppCompat.Light" />	//将弹出的菜单项设置为浅色主题
```

接着再在Activity里面设置使用Toolbar

```kotlin
setSupportActionBar(toolbar)
```

这样一来尽管Toolbar在外观上与原先的ActionBar别无二致，但是Toolbar已经具备了实现Material Design效果的能力。

其次还可以通过一些其他设定来改变Toolbar上面的一些功能。

在AndroidManifest.xml中，我们可以添加下面的属性指定Toolbar中现实的文字内容

```xml
<activity
    ...
    android:label="Fruits">
    ...
</activity>
```

另外，我们还可以设置一些action按钮来让Toolbar显得不那么单调，只需在res目录下新建menu文件夹创建menu文件即可

```xml
<?xml version="1.0" encoding="utf-8"?>
<menu
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <item
        android:id="@+id/boat"
        android:icon="@drawable/ic_action_directions_boat"
        android:title="boat"
        app:showAsAction="always" />
    <item
        android:id="@+id/bus"
        android:icon="@drawable/ic_action_directions_bus"
        android:title="bus"
        app:showAsAction="ifRoom" />
    <item
        android:id="@+id/car"
        android:icon="@drawable/ic_action_directions_car"
        android:title="car"
        app:showAsAction="never"/>
</menu>
```

需要注意的是app:showAsAction的选值有不同的意义：always表示永远显示在Toolbar中，如果屏幕空间不够则不显示；ifRoom表示屏幕空间足够的情况下显示在Toolbar中，不够的话就先是在菜单当中；never表示永远显示在菜单当中。而且显示在Toolbar中的按钮只会显示图标，菜单中的按钮只会显示文字。

```kotlin
override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.toolbar, menu)
    return true
}
```

最后再为各个按钮添加点击事件即可

## 2.滑动菜单

### （一）DrawerLayout

活动菜单就是将一些菜单选项隐藏起来，通过滑动的方式将菜单显示出来，可以使用DrawerLayout控件实现滑动菜单。

DrawerLayout是一个布局，在布局中允许放入两个直接子控件；第一个子控件时主屏幕中显示的内容，第二个子控件是滑动菜单中现实的内容，例如

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.drawerlayout.widget.DrawerLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/drawerlayout"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"

            <androidx.appcompat.widget.Toolbar
                android:id="@+id/toolbar"
                android:layout_width="match_parent"
                android:layout_height="?attr/actionBarSize"
                android:background="?attr/colorPrimary"
                android:theme=
                       "@style/ThemeOverlay.AppCompat.Dark.ActionBar"
                app:popupTheme="@style/ThemeOverlay.AppCompat.Light"/>

	</FrameLayout>
           
	<TextView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_gravity="start"
        android:background="#FFF"
        android:text="This is menu"
        android:textSize="30sp" />

</androidx.drawerlayout.widget.DrawerLayout>
```

DrawerLayout布局中放任意控件都是可以的，但是有一点需要注意，那就是滑动菜单中的”layout_gravity“属性是必须指定的，因为这个属性决定了滑动菜单是在屏幕的左边（left）还是右边（right）；如果指定的是start，那么就会按照语言的阅读顺序弹出。

但初始的DrawerLayout设定是只有在屏幕的左侧边缘拖动时才会将菜单拖动出来，显然用户是有可能不知道这个功能的，因此可以添加一个按钮告知用户

```kotlin
private lateinit var drawerLayout: DrawerLayout

override fun onCreate(savedInstanceState: Bundle?) {
    ...
    supportActionBar?.let {
        it.setDisplayHomeAsUpEnabled(true)
        it.setHomeAsUpIndicator(android.R.drawable.ic_menu_sort_by_size)	//这里使用了系统自带的图标
    ...
}
...
override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> drawerLayout.openDrawer(GravityCompat.START)
   ...
    }
```

调用getSupportActionBar()方法得到了ActionBar的实例（虽然ActionBar是由Toolbar实现的），接着在ActionBar不为空的情况下调用setDisplayHomeAsUpEnabled()方法显示导航按钮，再调用setHomeAsUpIndicator()方法放置导航按钮（这个按钮默认就是叫”home“按钮），接着重新设置home按钮的图案，最后在点击事件中调用DrawerLayout的openDrawer()方法将滑动菜单展示出来，传入一个与xml文件中定义一样的GravityCompat.START

### （二）NavigationView

添加依赖

```kotlin
implementation("com.google.android.material:material:...")
implementation("de.hdodenhof:circleimageview:...") 	//用于转换圆形图片
```

可以在NavigationView里插入menu布局实现菜单布局

```xml
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <group android:checkableBehavior="single">
        <item
            android:id="@+id/navCall"
            android:icon="@drawable/ic_action_phone"
            android:title="Call" />
        <item
            android:id="@+id/navFriends"
            android:icon="@drawable/ic_action_group"
            android:title="Friends" />
        <item
            android:id="@+id/navLocation"
            android:icon="@drawable/ic_action_location_on"
            android:title="Location" />
        <item
            android:id="@+id/navTask"
            android:icon="@drawable/ic_action_mail"
            android:title="Mail" />
        <item
            android:id="@+id/navTask"
            android:icon="@drawable/ic_action_content_paste"
            android:title="Tasks" />
    </group>
</menu>
```

上面是一个menu类的布局文件，group表示一个组，checkableBehavior指定为single表示组中的所有菜单项只能单选。

接着插入一个header布局用于显示滑动菜单上部的图片（好看！！）

```xml
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="180dp"
    android:padding="10dp"
    android:background="?attr/colorPrimary">
    <de.hdodenhof.circleimageview.CircleImageView
        android:id="@+id/iconImage"
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:src="@drawable/ic_gtr"
        android:layout_centerInParent="true" />
    <TextView
        android:id="@+id/mailText"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        android:text="2650535269@qq.com"
        android:textColor="#FFF"
        android:textSize="14sp" />
    <TextView
        android:id="@+id/userText"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_above="@id/mailText"
        android:text="Evan He"
        android:textColor="#FFF"
        android:textSize="14sp" />
</RelativeLayout>
```

这样就创建好了NavigationView的两个布局，接下来将原本放了TextView的DrawerDesign的第二个子控件替换为

```xml
<com.google.android.material.navigation.NavigationView
    android:id="@+id/navView"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:layout_gravity="start"
    app:headerLayout="@layout/nav_header"
    app:menu="@menu/nav_menu"/>
```

即可。

接着再在Activity中处理菜单项的点击事件即可

```kotlin
binding.navView.setCheckedItem(R.id.navCall)	//将某个菜单项默认选中
binding.navView.setNavigationItemSelectedListener {	//设置选中事件的监听器
    drawerLayout.closeDrawers()	//暂时设置为关闭滑动菜单
    true
}
```

## 3.悬浮按钮和可交互提示

### （一）悬浮按钮FloatingActionButton

```xml
<com.google.android.material.floatingactionbutton.FloatingActionButton
    android:id="@+id/fab"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_gravity="bottom|end"
    android:layout_margin="16dp"
    android:src="@drawable/ic_action_done"
    android:elevation="8dp"/>
```

app:elevation属性用于给FloatingActionButton设定一个高度值。高度值越大，投影范围也就越大，但投影效果越淡。

### （二）Snackbar

点击按钮时一般都会设置为弹出一个Toast，但普通的Toast并没有给用户做其他操作的余地；而Snackbar在用法上与Toast基本相似，只不过可以额外增加一个按钮的点击事件。

```kotlin
binding.fab.setOnClickListener { view ->
    Snackbar.make(view, "Data deleted", Snackbar.LENGTH_SHORT)
        .setAction("Undo") {
            Toast.makeText(this, "Data restored", Toast.LENGTH_SHORT).show()
        }
        .show()
}
```

这里调用了Snackbar的make()方法，这个方法的第一个参数传入一个View，这个view可以是当前界面的任意一个view，Snackbar会使用这个View自动查找最外层的布局，用于展示提示信息。setAction()方法可以与用户进行交互。

### （三）CoordinatorLayout

CoordinatorLayout基本与FrameLayout一样，但是有一些material能力：CoordinatorLayout可以监听所有子控件的各种事件，并自动做出最为合理的相应。

把<FrameLayout>换成<androidx.coordinatorlayout.widget.CoordinatorLayout>这样当点击悬浮按钮弹出Snackbar时悬浮按钮会向上移动一些距离，Snackbar就不会遮挡住。

## 4.卡片式布局

### （一）MaterialCardView

MeterialCardView也是一个FrameLayout，不过额外提供了圆角和阴影效果。

```xml
<com.google.android.material.card.MaterialCardView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_margin="5dp"
    app:cardCornerRadius="4dp"	//圆角
    app:cardElevation="5dp">	//卡片悬浮高度

    <LinearLayout
        android:orientation="vertical"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <ImageView
            android:id="@+id/fruitImage"
            android:layout_width="match_parent"
            android:layout_height="100dp"
            android:scaleType="centerCrop" />
        <TextView
            android:id="@+id/fruitName"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center_horizontal"
            android:layout_margin="5dp"
            android:textSize="16sp" />
    </LinearLayout>

</com.google.android.material.card.MaterialCardView>
```

接着新建一个RecyclerViewAdapter，用于插入上面的卡片

```kotlin
class FruitAdapter(val context: Context, val fruitList: List<Fruit>) : RecyclerView.Adapter<FruitAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val fruitImage: ImageView =view.findViewById(R.id.fruitImage)
        val fruitName: TextView =view.findViewById(R.id.fruitName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.fruit_item, parent, false)
        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            val fruit = fruitList[position]
            val intent = Intent(context, FruitActivity::class.java).apply {
                putExtra(FruitActivity.FRUIT_NAME, fruit.name)
                putExtra(FruitActivity.FRUIT_IMAGE_ID, fruit.imageId)
            }
            context.startActivity(intent)
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fruit = fruitList[position]
        holder.fruitName.text = fruit.name
        Glide.with(context).load(fruit.imageId).into(holder.fruitImage)
    }

    override fun getItemCount() = fruitList.size
}
```

最后再在Activity里面创建实例的mutableList，将数据插入recyclerView中

```kotlin
//省略定义集合的步骤
initFruits()
    val layoutManager = GridLayoutManager(this, 2)	//第一个参数是context，第二个参数是列数，表示一行中显示多少个item
    binding.recyclerView.layoutManager = layoutManager
    val adapter = FruitAdapter(this, fruitList)
    binding.recyclerView.adapter = adapter
}
private fun initFruits() {
    fruitList.clear()
    repeat(50) {
        val index = (0 until fruits.size).random()
        fruitList.add(fruits[index])
    }
}
```

这样一来，item就以一行两个的形式展示在app当中了，但是会发现recyclerView充满了整个屏幕，连Toolbar也被挡住了，可以用AppBarLayout进行优化。

### （二）AppBarLayout

AppBarLayout实际上是一个垂直方向的LinearLayout，但做了很多滚动事件的封装并应用了一些Material Design的设计理念。

```xml
<com.google.android.material.appbar.AppBarLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content">

    <androidx.appcompat.widget.Toolbar
        android:id="@+id/toolbar"
        android:layout_width="match_parent"
        android:layout_height="?attr/actionBarSize"
        android:background="?attr/colorPrimary"
        android:theme="@style/ThemeOverlay.AppCompat.Dark.ActionBar"
        app:popupTheme="@style/ThemeOverlay.AppCompat.Light"
        app:layout_scrollFlags="scroll|enterAlways|snap"/>	//当页面整体向上滑动时，Toolbar会隐藏|当页面整体向下滑动时，Toolbat会跟着向下滑动重新显示|当Toolbar没有完全隐藏或显示时，根据当前滚动的距离自动选择隐藏还是显示

</com.google.android.material.appbar.AppBarLayout>

<androidx.recyclerview.widget.RecyclerView
		... 
                                           app:layout_behavior="@string/appbar_scrolling_view_behavior" />
```

这里将Toolbar嵌套进AppBarLayout里面并在RecyclerView里添加一条app属性。

## 5.下拉刷新

添加依赖

```kotlin
implementation("androidx.swiperefreshlayout:swiperefreshlayout:...")
```

SwipeRefreshLayout的用法就是把列表嵌套进去即可。

```xml
<androidx.swiperefreshlayout.widget.SwipeRefreshLayout
    android:id="@+id/swipeRefresh"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:layout_behavior="@string/appbar_scrolling_view_behavior">

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerView"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</androidx.swiperefreshlayout.widget.SwipeRefreshLayout>
```

由于现在RecyclerView变成了SwipeRefreshLayout的子控件，所以原本RecyclerView中的app属性也要移到SwipeRefreshLayout中。接着修改Activity中的代码

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    ... 		  binding.swipeRefresh.setColorSchemeResources(com.google.android.material.R.color.material_dynamic_primary10)	//设置下拉刷新进度条的颜色
    binding.swipeRefresh.setOnRefreshListener {	//设置下拉刷新的监听器
        refreshFruits(adapter)
    }
}

private fun refreshFruits(adapter: FruitAdapter) {
    thread {
        Thread.sleep(2000)	//将线程沉睡两秒
        runOnUiThread {		//切换回主线程
            initFruits()	//重新生成数据
            adapter.notifyDataSetChanged()	//通知数据发生了变化
            binding.swipeRefresh.isRefreshing = false	//传入false表示刷新时间结束并隐藏进度条
        }
    }
}
```

## 6.可折叠式标题栏--CollapsingToolbarLayout

CollapsingToolbarLayout只能是AppBarLayout的直接子布局，二AppBarLayout又必须是CoordinatorLayout的子布局。下面是运用CollapsingToolbarLayout实现的一个子页面。

```xml
<androidx.coordinatorlayout.widget.CoordinatorLayout
	//最外层为CoordinatorLayout                                               xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">	

    <com.google.android.material.appbar.AppBarLayout
        //第二层是AppBarLayout                                 					android:id="@+id/appBar"
        android:layout_width="match_parent"
        android:layout_height="250dp" >		//高度指定为250dp（好看）

        <com.google.android.material.appbar.CollapsingToolbarLayout
            //最里层是CollapsingToolbarLayout             
            android:id="@+id/collapsingToolbar"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:theme="@style/ThemeOverlay.AppCompat.Dark.ActionBar"
            app:contentScrim="?attr/colorPrimary"	//用于指定CollapsingToolbarLayout在趋于折叠状态以及折叠之后的颜色
            app:layout_scrollFlags="scroll|exitUntilCollapsed">		//scroll表示CollapsingToolabrLayout会随着页面的滚动一起滚动|exitUntilCollapsed表示当CollapsingToolbarLayout随着滚动完成折叠之后就保留在界面上，不再移出屏幕

            <ImageView
                android:id="@+id/fruitImageView"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:scaleType="centerCrop"
                app:layout_collapseMode="parallax" />	//app:layout_collapseMode="parallax"用于指定图片在折叠过程中产生一定的错位偏移（好看）

            <androidx.appcompat.widget.Toolbar
                android:id="@+id/toolbar"
                android:layout_width="match_parent"
                android:layout_height="?attr/actionBarSize"
                app:layout_collapseMode="pin" />	//app:layout_collapseMode="pin"用于指定当前控件在CollapsingToolbarLayout在折叠过程中的折叠模式，pin表示在折叠的过程中位置始终保持不变

        </com.google.android.material.appbar.CollapsingToolbarLayout>

    </com.google.android.material.appbar.AppBarLayout>

    <androidx.core.widget.NestedScrollView
        //与ScrollView类似，不过在ScrollView的基础上还增加了嵌套响应滚动事件的功能
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_behavior="@string/appbar_scrolling_view_behavior" >

        <LinearLayout
            android:orientation="vertical"
            android:layout_width="match_parent"
            android:layout_height="wrap_content">

            <com.google.android.material.card.MaterialCardView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginBottom="15dp"
                android:layout_marginLeft="15dp"
                android:layout_marginRight="15dp"
                android:layout_marginTop="35dp"
                app:cardCornerRadius="4dp" >

                <TextView
                    android:id="@+id/fruitContentText"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_margin="10dp"/>

            </com.google.android.material.card.MaterialCardView>

        </LinearLayout>

    </androidx.core.widget.NestedScrollView>

    <com.google.android.material.floatingactionbutton.FloatingActionButton
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_margin="16dp"
        android:src="@drawable/ic_action_sms"
        app:layout_anchor="@id/appBar"
        app:layout_anchorGravity="bottom|end"/>

</androidx.coordinatorlayout.widget.CoordinatorLayout>
```

以上就是一个点击recyclerView后跳转到的页面的布局编写，下面还要处理RecyclerView的点击事件。修改FruitAdapter中的代码

```kotlin
override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
    ...
    val holder = ViewHolder(view)	
    holder.itemView.setOnClickListener {	//给fruit_item的最外层布局注册一个监听器
        val position = holder.adapterPosition
        val fruit = fruitList[position]
        val intent = Intent(context, FruitActivity::class.java).apply {
            putExtra(FruitActivity.FRUIT_NAME, fruit.name)
            putExtra(FruitActivity.FRUIT_IMAGE_ID, fruit.imageId)
        }	//获取点击项的水果名和水果图片资源id，传入Intent中再启动FruitActivity
        context.startActivity(intent)
    }
    return holder
}
```

最后是在FruitActivity中接收传入的水果名称和图片资源id

```kotlin
class FruitActivity : AppCompatActivity() {

    companion object {
        const val FRUIT_NAME = "fruit_name"
        const val FRUIT_IMAGE_ID = "fruit_image_id"
    }

    private lateinit var binding: ActivityFruitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFruitBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val fruitName = intent.getStringExtra(FRUIT_NAME) ?: ""
        val fruitImageId = intent.getIntExtra(FRUIT_IMAGE_ID, 0)
        //获取传入的水果名称和水果图片资源id
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.collapsingToolbar.title = fruitName
        binding.collapsingToolbar.setExpandedTitleColor(Color.WHITE)
       //设置 折叠标题栏展开时的title的字体颜色
        binding.collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE)
        //设置折叠标题栏折叠时的title的字体颜色
        Glide.with(this).load(fruitImageId).into(binding.fruitImageView)
        binding.fruitContentText.text = generateFruitContent(fruitName)
    }

    override fun onOptionsItemSelected(item: MenuItem) : Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun generateFruitContent(fruitName: String) = fruitName.repeat(500)

}
```
