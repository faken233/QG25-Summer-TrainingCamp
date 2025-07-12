# ViewBinding

## 添加配置

使用viewBinding之前需要在build.gradle文件中添加配置

```kotlin
android {
	...
	buildFeatures {
        viewBinding = true
    }
}
```

添加配置之后AndroidStudio会自动为每个布局layout文件生成对应的Binding类，原本如果需要调用button1需要这样写代码

```kotlin
val button : Button = findViewById(R.id.button1)
button1.setOnClickListener {
	Toast.makeText(this, "Hello World", Toast.LENGTH_SHORT).show()
}
```

而在使用ViewBinding之后可以这样写

```kotlin
val binding = ActivityMainBinding.inflate(layoutInflater)	//ActivityMainBinding是根据布局	文件activity_main.xml自动生成的ViewBinding类，用于访问内部控件
setContentView(binding.root)	//root是ActivityMainBinding里的属性,指向解析后的布局根视图
binding.button1.setOnClickListener {	//通过binding直接访问布局里id为button1的按钮控件
     Toast.makeText(this, "Hello World", Toast.LENGTH_SHORT).show()
}
```

## 注意在不同情况下使用ViewBinding时的差异

### 1.Activity

​	当我们在Activity中使用ViewBinding时，它的作用是设置整个页面的内容视图。以下是以Activity为例子的ViewBinding的用法：

```kotlin
class Activity : AppCompatActivity {
	override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ...
    }

}
```

### 2.在Adapter（比如ListView、RecyclerView的Adapter）中返回binding.root

​	当我们再写RecyclerView.Adapter或ListView.Adapter时，getView的作用是为列表的**单个条目**提供视图。原理是Adapter里的getView(或onCreateViewHolder)需要返回“单个条目的视图”给列表容器渲染，而binding.root同样是条目布局的根视图，所以返回binding.root才能把数据渲染到界面上。以下是以Adapter为例子的ViewBinding的用法：

```kotlin
class IconicAdapter(activity: Activity, resourceId: Int, data: List<Iconic>) :
    ArrayAdapter<Iconic>(activity, resourceId, data){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val binding = if(convertView == null) {
            IconicItemBinding.inflate(LayoutInflater.from(context), parent, false)
        } else {
            IconicItemBinding.bind(convertView)
        }
        val iconic = getItem(position)
        if (iconic != null) {
            binding.iconicImage.setImageResource(iconic.imageId)
            binding.iconicName.text = iconic.name
        }
        return binding.root
    }
}
```

一个RecyclerView.Adapter的ViewBinding例子

```kotlin
class IconicAdapter(val iconicList: List<Iconic>) : RecyclerView.Adapter<IconicAdapter.ViewHolder>() {
    
    inner class ViewHolder(val binding: IconicItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
        val binding = IconicItemBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val iconic = iconicList[position]
        holder.binding.iconicImage.setImageResource(iconic.imageId)
        holder.binding.iconicName.text = iconic.name
    }

    override fun getItemCount(): Int {
        return iconicList.size
    }
}
```

