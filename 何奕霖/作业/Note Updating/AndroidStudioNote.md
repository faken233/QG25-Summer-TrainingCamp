# AndroidStudio 笔记



## 1.AndroidManifest.xml中注册活动信息:

与活动信息的<activity>(如果这其中的android:exported显示为"false",则将其修改为"true")对应,在末尾加上</activity>然后在之间加上如下代码:

```java
<intent-filter>
    <action android:name="android.intent.action.MAIN"/>
    <category android:name="android.intent.category.LAUNCHER"/>
</intent-filter>
```

这样一来该活动便被成功地注册,否则会在运行时报错.

## 2.基础的Toast语句(用于输出信息):

我们在创建了一个控件,例如一个按钮之后可以先为其注册一个监听器(用于监听按钮的动作),用Toast可以在应用中输出话语:

```java
protected void onCreate(Bundle savedInstanceState) {
    // 调用父类的 onCreate 方法，以确保父类的初始化逻辑被执行
    super.onCreate(savedInstanceState);
    // 设置当前 Activity 的布局为 activity_main.xml 中定义的布局
    setContentView(R.layout.activity_main);
    // 通过 findViewById 方法找到布局中 id 为 button1 的按钮，并将其强制转换为 Button 类型
    Button button_1 = (Button) findViewById(R.id.button1);
    // 为按钮设置一个点击事件监听器
    button_1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 当按钮被点击时，使用 Toast 类显示一个短时间的消息
            Toast.makeText(MainActivity.this, "You clicked button 1", Toast.LENGTH_SHORT).show();
        }
    });
}
```

## 3.在活动中使用Menu:

1.创建步骤:res->New->Directory->输入文件夹名称menu->在该文件夹下右击->New->Menu resource file->输入一个名称(如"main")

接下来在该main.xml下编辑要显示出来的选项:

```xml
<item
    android:id="@+id/add_item"			//选项的id时add_item
    android:title="add"/>				//选项现实的文本为"add"
```

接着回到主活动java文件,先import一个import android.view.Menu;说明要进行Menu的编辑;要想让编辑的Menu显示出来,需要重写onCreateOptionsMenu方法:

```java
public boolean onCreateOptionsMenu(Menu menu) {
    // 使用菜单填充器（MenuInflater）将菜单资源文件 main.xml 填充到传入的 Menu 对象中
    getMenuInflater().inflate(R.menu.main, menu);
    // 返回 true 表示菜单已经成功创建并准备好显示
    return true;
}
```

现在已经设置好了一个菜单控件,可以再给它设置一定的功能.

## 4.销毁活动:

按back按键可以销毁当前活动并回到上一层活动.与此同时也可以使用"finish();"来销毁活动:

```java
button_1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
       finish();			//按下button_1时销毁当前活动
    }
});
```

## 5.显示intent进行活动间的穿梭:

intent是Android程序中各组件之间进行交互的一种重要方式,分为**显示intent**和**隐式intent**.

在进行活动的穿梭之前,还要创建一个新的活动,这里右键com......(java文件下面的第一个子文件)来新建一个empty  views activity,再按照基本步骤进行注册,值得注意的是,AndroidManifest.xml中已经帮我们注册好了,我们只需做下面的修改:

```xml
<activity
    android:name=".SecondActivity"
    android:exported="true" >
</activity>
```

接下来对一个控件添加intent,还是以button_1为载体:

```java
button_1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);//创建一个显示intent实现跳转
        startActivity(intent);		//通过startActivity执行intent
    }
});
```

## 6.intent的其他用法:

打开某个网站:

```java
button_1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //这里创建了一个隐式Intent,并将其动作设置为Intent.ACTION_VIEW,表示请求查看指定的数据
        intent.setData(Uri.parse("http://www.baidu.com"));
        //Uri.parse("http://www.baidu.com") 会将字符串形式的URL解析成Uri对象,然后将这个Uri对象设置到Intent中,告诉系统要查看的是百度的网页
        startActivity(intent);
    }
});
```

拨号:

```java
button_1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        //创建隐式intent,并将其动作设置为打开设备的拨号界面
        intent.setData(Uri.parse("tel:12345"));
        startActivity(intent);
    }
});
```

## 7.活动的生命周期:

**活动状态:**

​	运行状态: 当一个活动处于返回栈的栈顶时,此时活动就处于运行状态

​	暂停状态: 活动不处于栈顶时,就是暂停状态;当内存极低的时候,系统会考虑回收这种活动

​	停止状态: 活动不处于栈顶且完全不可见,就进入了停止状态

​	销毁状态: 活动从返回栈中移除就变成了销毁状态,系统最倾向于回收处于这种状态的活动来保证内存充足

**活动的生存期:**

​	onCreate(): 完成活动的初始化操作;如加载布局,绑定事件

​	onStart(): 在活动由不可见变为可见的时候调用

​	onResume(): 在活动准备好和用户进行交互时调用

​	onPause(): 在系统准备去启动或者恢复另一个活动的时候调用

​	onStop(): 在活动完全不可见的时候调用

​	onDestroy(): 在活动被销毁之前调用,之后活动将会变成销毁状态

​	onRestart(): 在活动由停止状态变为运行状态之前调用

​	以上方法除了onRestart()方法之外都是两两对应的,所以可以分为三种生存期:**完整生存期,可见生存期,前台生存期**

## 8.活动的启动模式:(可以观察logCat)

(1)**standard**:

​	standard是活动默认的启动方式,对于使用standard模式的活动,**系统不会在乎这个活动是否存在于返回栈中,每次启动都会新建一个新的实例**.

```java
button_1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);	//跳转到当前页面
        startActivity(intent);	//执行该intent
    }
});
```

接下来每当点击一次button_1,系统都会新建一个MainActivity实例,这是因为standard启动模式并不会在意返回栈中是否已经存在该实例.

也就是说,要想回到开始的那个MainActivity页面就需要点击多次back.

(2)**singleTop**:

​	singleTop启动模式**在启动活动时会检查返回栈,如果返回栈的栈顶已经是当前活动,那就认为可以直接使用他,而不会再创建新的活动实例:**

```xml
<activity
    android:name=".MainActivity"
    android:launchMode="singleTop"	//修改启动模式为"singleTop"
    android:exported="true">
    <intent-filter>...
```

注意,给一个活动设置非默认模式的时候,要在AndroidManifest.xml中对应的活动下修改"Android:launchMode"

然后运行程序就会发现无论点击多少次button_1,始终都停留在当前活动页面.

(3)**singleTask**:

​	singleTask模式会**在每次启动实例前检查返回栈中是否有该活动的实例,如果发现存在则直接使用**:

```xml
<activity
    android:name=".MainActivity"
    android:launchMode="singleTask"		//修改启动模式为singleTask
    android:exported="true">
    <intent-filter>...
```

......

## 9.UI界面

(1)**TextView**:

​	TextView主要用于在界面上打印一段文本信息,其在布局界面的代码如下:

```xml
<TextView
    android:id="@+id/text_view"				//id
    android:layout_width="match_parent"		//根据父布局调整控件大小
    android:layout_height="wrap_content"	//根据包裹的文本内容调整控件大小
    android:text="This is TextView"/>		//控件内显示的文本
```

可以看到,代码中有**android:layout_width**和**android:layout_height**这两个属性,分别表示**控件的宽度和高度**.这里两个属性有三种可选值:**wrap_content**, **match_parent**和**fill_parent**,其中fill_parent和match_parent的意义相同,都表示为**根据父布局来决定当前控件的大小**,而wrap_content表示**让当前控件的大小能够刚好包住里面的内容**.

除此之外,因为TextView的文本内容默认是处于页面左上方的,所以可以通过以下设定来调整TextView:

​	a.android:layout_gravity

​		android:gravity用于**指定文字的对齐方式**,可选值有top,bottom,left,right和center等,**可以用"|"来同时指定多个值**.

​	b.android:textSize & android:textColor

​		android:textSize用于指定文本字体的大小,以"sp"为单位(如"24sp"),android:textColor用于指定文本字体的颜色(如"#00ff00").

(2)**Button**:

​	Button是程序用于和用户交互的一个重要控件,其在布局界面的代码如下:

```xml
<Button
    android:id="@+id/button1"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="Button 1"/>
```

注意,虽然代码中对Button的文本设置为了"Button 1"但是运行程序后会发现显示的却是"BUTTON 1",这是由于系统会对Button中的所有英文字母自动进行大写转换,可以通过下面的代码禁用:

```xml
<Button
    android:id="@+id/button1"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="Button 1"
    android:textAllCaps="false"/>
```

然后可以在活动为button注册监听器:

```java
Button button_1 = (Button) findViewById(R.id.button1);
button_1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
    	...
    }
});
```

也可以使用实现接口的方式来进行注册:

```java
	...
	Button button_1 = (Button) findViewById(R.id.button1);
    button_1.setOnClickListener(this);
}

@Override
public void onClick(View v){
    if(v.getId()==R.id.button1){		//接收按下按钮的信号
    	...
    }
}
```

需要注意的是button_1.setOnClickListener(this);中的this代表当前的实例,需要onClickListener接口,所以为了让这个实例实现onClickListener接口,应修改类定义为**public class UI extends AppCompatActivity implements View.OnClickListener**.

**注意注意!这里的"UI"是该活动的名称!**

(3**)EditText**:

​	EditText是程序用于和用户进行交互的一个重要控件,允许用户在控件里输入和编辑内容:

```xml
<EditText
    android:id="@+id/edit_text"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>
```

但平时在输入框输入东西之前可以常常看到有提示词,比如"password".可以在xml代码中加入**adnroid:hint="提示词"**就可以显示提示词了,并且在输入时这些提示词会自动消失;再者,由于设定的layout_height是wrap_content,所以不管输入多少内容,文本框总是能包住全部内容,也就是说会一直拉长.所以可以在代码中添加**android:maxLines="行数"**来限制总行数,当内容超过设定的行数时,内容就会向上滚动.

​	与此同时,还可以设定EditText与Button等控件进行交互.先在活动大类中private定义一个EditText变量,然后再添加Button内容:

```java
public class UI extends AppCompatActivity implements View.OnClickListener {

    private EditText et;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_layout);
        Button button_1 = (Button) findViewById(R.id.button1);
        et = (EditText) findViewById(R.id.edit_text);		//注册监听器
        button_1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        if(v.getId()==R.id.button1){
            String inputText = et.getText().toString();		//将输入的内容转换为字符串
            Toast.makeText(UI.this, inputText,Toast.LENGTH_SHORT).show();	//打印输入的内容
        }
    }
}
```

(4)**ImageView**:

​	ImageView是用于**在界面上展示图片的控件**.图片通常都是放在drawable目录的,在res目录下新建一个drawable-xhdpi目录,然后将准备好的图片复制到这个目录下(**注意一定要更改名字**),在布局界面设置一个ImageView控件:

```xml
<ImageView
    android:id="@+id/image_view"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:src="@drawable/img_1"/>			//drawable目录下的img_1
```

(5)**ProgressBar**:

​	ProgressBar用于在界面上显示一个进度条,表示程序正在加载一些数据.

```xml
<ProgressBar
    android:id="@+id/progress_bar"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>
```

但是进度条不能一直存在,所以可以通过android:visibility进行指定,可选值有3种:visible, inviseble和gone.visible表示控件是可见的,是默认值;invisible表示控件不可见,会占用屏幕空间;gone表示控件不仅不可见,而且会占用屏幕空间.设置控件的可见性通过setVisibility()方法实现,可传入**View.VISIBLE**,**View.INVISIBLE**和**View.GONE**这三种值.除此之外,还可以通过style属性修改进度条的样式.

```xml
<ProgressBar
    android:id="@+id/progress_bar"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    style="?android:attr/progressBarStyleHorizontal"		//设置进度条样式为水平进度条
    android:max="100"		//进度条最大值为100
    />
```

另外,在水平进度条里,**不断增加的进度**显示是一个关键步骤:

```java
...
    int progress = progressBar.getProgress();
    progress = progress +10;
    progressBar.setProgress(progress);
...
```

(6)**AlertDialog**:

​	AlertDialog可以在当前的界面**弹出一个对话框**,并且这个对话框是置于所有界面元素之上的,**能够屏蔽掉其他控件的交互能力.**

```java
...
if(v.getId()==R.id.button_1){
    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);	
    //用AlertDialog.Builder来尽力一个AlertDialog实例
    dialog.setTitle("Intention");	//设置大标题为Intention
    dialog.setMessage("do you want to quit?");	//设置小内容为do you want to quit?
    dialog.setCancelable(false);	//false代表不能通过back键返回
    dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {	//设置确定按钮的点击事件
        @Override
        public void onClick(DialogInterface dialog, int which) {
            finish();	//这里设置为了销毁活动
        }
    });
    dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {	//设置取消按钮的点击事件
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();	//这里设置为了关闭AlertDialog
        }
    });
    dialog.show();	//将对话框显示出来
}
...
```

(7)**ProgressDialog**:

​	ProgressDialog的作用也是**弹出一个对话框**,其中含有一个进度条,一般**用于反馈给用户当前操作比较耗时的信息**.

```java
...
if(v.getId()==R.id.button_1){
    ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
    //用ProgressDialog.Builder来尽力一个ProgressDialog实例
    progressDialog.setTitle("Waiting");		//设置大标题
    progressDialog.setMessage("Loading...");	//设置小内容
    progressDialog.setCancelable(true);		//可以通过back键返回
    progressDialog.show();		//显示对话框
}
...
```

## 10.四种基本布局

(1)**线性布局(LinearLayout)**:
	线性布局会将它所包含的控件在线性方向上依次排列,以下是线性布局的属性**android:orientation**:
这个属性指定了控件的排列方向,比如vertical就是在垂直方向上排列;如果是horizontal,空间就会在水平方向上排列:

```xml
<LinearLayout...
     android:orientation="horizontal"
     ...

     //控件

</LinearLayout>
```

需要注意的是,如果排列方向是horizontal,控件就一定不能将宽度设置为match_parent,否则的话一个控件就会把水平方向占满,其他控件就没有放置的位置了.在布局文件中设置

```xml
<Button
    android:id="@+id/button_1"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_gravity="top"	//button_1处于页面顶部,center则是中间,bottom为底部
    android:text="Button 1"/>
```

​	**重要特性!!!**

​	android:layout_weight这个属性允许**使用比例的方式来指定控件的大小**,对适配屏幕有很重要的作用.

```xml
<EditText
    android:id="@+id/edit_text"
    android:layout_width="0dp"		//0dp可以避免由width来控制
    android:layout_height="wrap_content"
    android:layout_weight="1"
    android:hint="Type Something Here"/>
<Button
    android:id="@+id/button_1"
    android:layout_width="0dp"
    android:layout_height="wrap_content"
    android:layout_weight="1"
    android:text="send"/>
```

代码中EditText和Button的layout_weight值都为1,在布局中系统会**先把各个控件的weight的值相加,然后用每个控件的weight值除以总值获得控件的占比**,也就决定了空间的大小.另外,也可以将width和weight结合起来使用.

(2)**相对布局(RelativeLayout)**:

​	相对布局通过相对定位的方式让控件出现在布局的任何位置.有以下的属性:

​		android:layout_alignParentLeft="true(false)"		//处于父布局的左方

​		android:layout_alignParentTop="true(false)"		//处于父布局的上方

​		android:layout_alignParentRight="true(false)"	     //处于父布局的右方	

​		android:layout_alignParentBottom="true(false)"	 //处于父布局的底部

​		android:layout_centerInParent="true(false)"		//处于父布局的中心

这些布局属性是相对于父布局的属性,同样也有相对于控件的属性:

​		android:layout_above="@+id/(id)"				  //处于控件的上方

​		android:layout_below="@+id/(id)"				  //处于控件的下方

​		android:layout_toLeftOf="@+id/(id)"			      //处于控件的左侧

​		android:layout_toRightOf="@+id/(id)"			    //处于控件的右侧

除了这些之外,还有android:layout_alignRight(表示让一个控件的右边缘和另一个控件的右侧对齐)等.

(3)**帧布局(FrameLayout)**:

​	帧布局里的所有控件都会默认放在布局的左上角,但仍然可以用layout_gravity来指定控件的对齐方式.

(4)**百分比布局(PercentFrameLayout&PercentRelativeLayout)**:

​	由于相对布局和帧布局都无法使用layout_weight来指定控件大小,所以百分比布局是与它们结合的布局

## 11.**创建自定义控件**

(1)**引入布局**:

​	在layout布局中创建新的xml布局之后再在活动的xml布局文件中include就可以直接引入自建的布局

```xml
<include layout="@layout/title"/>		//引入了title.xml布局
```

(2)**创建自定义控件**:

​	创建自定义控件可以**规避同一功能的控件的代码重复书写**,通过新建TitleLayout(例子)继承自LinearLayout

```java
public class TitleLayout extends LinearLayout {

    public TitleLayout(Context context, AttributeSet attrs){		
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.title, this);		//构建LayoutInflater对象,调用inflate方法动态加载一个布局文件
        Button titleBack = (Button) findViewById(R.id.title_back);
        Button titleEdit = (Button) findViewById(R.id.title_edit);
        titleBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) getContext()).finish();
            }
        });
        titleEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Edit", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
```

除此之外,要想使用自定义控件,还需在主活动的xml布局中添加这个自定义控件

```xml
<com.example.uicustomviews.TitleLayout			//代表一个自定义的布局组件
    android:layout_width="match_parent"			//指定自定义布局在父布局里的宽度
    android:layout_height="wrap_content"/>
```

## 12.ListView:滚动信息

(1)**ListView的用法**:

​	在xml布局里建立一个ListView控件:

```xml
<ListView
    android:id="@+id/list_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
```

再然后在活动里添加一组数据;但是这些数据并不能直接传递给ListVIew,因此还需要借助适配器,比如ArrayAdaptor.比如说提供的数据都是字符串,所以将ArrayAdaptor的泛型设置为String,然后传入当前上下文,ListView子项布局的id.最后还要调用setAdaptor方法,将构建好的适配器对象传递进去.

```java
...
private String[] data = {"one","two","three","five","six","seven","eight","nine","ten","eleven","twelve",
                                "thirteen","fourteen","fifteen","sixteen","seventeen","eighteen"};
//添加一组数据
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ...
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(		//泛型与数据类型一致
                MainActivity.this, android.R.layout.simple_list_item_1, data
        );		//传入id
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);		//传递适配器对象

    }
...
```

(2)提升ListView的运行效率:

​	在之前的ListView活动中,getView()方法每次都将布局重新加载了一遍,因此当ListView快速滚动时,就会产生性能瓶颈.

​	

此处省略1万字笔记,由程序体现.



## 13.RecyclerView

## 14.制作Nine-Patch图片

​	Nine-Patch图片是一种被特殊处理过的png图片,能够指定哪些区域可以被拉伸,哪些区域不可以.

## 15.碎片(Fragment)

​	碎片是一种可以嵌入在活动中的UI片段,能让程序更加合理和充分地利用大屏幕的空间 .

具体步骤为:

1.新建碎片布局,注意要选择androidX.fragment.app.Fragment包下的Fragment

```java
import androidx.fragment.app.Fragment;
```

2.创建碎片的类,用LayoutInflater方法将定义的布局加载进来

```java
...
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle 		   savedInstanceState){		//重写onCreateView方法
    View view = inflater.inflate(R.layout.left_fragment, container, false);
    return view;
}
...
```

3.最后再修改主活动的xml布局,用<fragment>标签在布局中添加碎片

```xml
<fragment
    android:id="@+id/left_fragment"
    android:name="com.example.fragmenttest.LeftFragment"
    android:layout_width="0dp"
    android:layout_height="match_parent"
    android:layout_weight="1"/>
```

## 16.动态添加碎片

​	动态添加碎片主要分为5步:

1.**创建添加的碎片实例**

​	创建另一个碎片xml和类

```xml
<fragment
    android:id="@+id/left_fragment"
    android:name="com.example.fragmenttest.LeftFragment"
    android:layout_width="0dp"
    android:layout_height="match_parent"
    android:layout_weight="1"/>

<FrameLayout
    android:id="@+id/right_layout"
    android:layout_width="0dp"
    android:layout_height="match_parent"
    android:layout_weight="1">
```

将原本的right_fragment替换成一个frameLayout

2.**获取FragmentManager,在活动中可以直接通过调用getSupportFragmentManager方法得到.**

3.**开启一个事务,通过调用beginTransaction方法开启.**

4.**向容器内添加或替换碎片,一般使用replace方法实现,需要传入容器的id和待添加的碎片实例.**

5.**提交事务,调用commit方法来完成**

具体代码如下:

```java
protected void onCreate(Bundle savedInstanceState) {
    ...
    replaceFragment(new RightFragment());
}

@Override
public void onClick(View v){
    ...
        replaceFragment(new AnotherRightFragment());	//点击按钮时替换碎片
    }
}

private void replaceFragment(Fragment fragment){
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    transaction.replace(R.id.right_layout, fragment);
    transaction.commit();
}
```

## 17.在碎片中模拟返回栈

 如果不添加返回栈,那在上一个例子中点击back键之后就会直接退出整个程序,可以在主活动的点击事件中修改关于Fragment的部分代码:

```java
private void replaceFragment(Fragment fragment){
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction transaction = fragmentManager.beginTransaction();   //transaction是一次性的,commit之后如果还想做其他操作需用到transaction重新获取对象
    transaction.replace(R.id.right_layout, fragment);
    transaction.addToBackStack(null);		//这里的"null"用于描述返回栈的状态,一般传入null即可	
    transaction.commit();
}
```

## 18.碎片和活动之间进行通信

碎片和活动是各自存在于不同的类当中的

下面是FragmentManager提供的类似于findViewById的方法专门用于从布局文件中获取碎片的实例:

```````java
RightFragment rightFragment = (RightFragment) getSupportFragmentManager().findViewById(R.id.right_fragment);
```````

在碎片中调用活动里的方法:

```java
MainActivity activity = (MainActivity) getActivity();
```

另外,当碎片中需要使用Context对象时,也可以使用getActivity方法,因为获取到的活动本身就是一个Context对象.

## 19.碎片的生命周期

和活动一样,碎片也有自己的生命周期.

碎片有以下几种状态:

​	1.**运行状态**:当一个碎片式可见的,而且它所关联的活动正处于运行状态,该碎片也处于运行状态;

​	2.**暂停状态**:当一个活动进入暂停状态时,与它相关联的可见碎片就会进入到暂停状态;

​	3.**停止状态**:通过调用FragmentTranction的remove,replace方法将碎片从活动中移除.但如果在事务提交之前调用addToBackStack方法,这时碎片也会进入到停止状态.总的来说,进入停止状态的碎片对于用户来说是完全不可见,有可能会被系统回收.

​	4.**销毁状态**:碎片总是依附于活动而存在的,因此当活动被销毁时,与它关联的碎片也会进入销毁状态.或者通过调用FragmentTranction的remove,replace方法将碎片从活动中移除,但在事务提交之前并没有调用addToBackStack方法,这时的碎片也会进入到销毁状态.

**Fragment中也提供了几种回调方法**:

​	onAttach():当碎片和活动建立关联的时候调用.

​	onCreateView():为碎片创建视图(加载布局)时调用.

​	onActivityCreated():确保与碎片相关的活动一定已经创建完毕的时候调用

​	onDestroyView():当与碎片关联的视图被移除的时候调用.

​	onDetach():当碎片和活动解除关联的时候调用.

![image-20250327171635219](C:\Users\Akinass\AppData\Roaming\Typora\typora-user-images\image-20250327171635219.png)

## 20.动态加载布局的技巧

动态加载布局的技巧可以让程序根据屏幕大小决定加载哪个布局.

**(1)使用限定符:**在上一个例子中,将activity_main.xml的代码修改如下,只保留一个左侧碎片并让其充满整个父布局:

```xml
<fragment
    android:id="@+id/left_fragment"
    android:name="com.example.fragmenttest.LeftFragment"
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
```

在res目录下新建一个layout-large文件夹,在这个文件夹下新建一个布局,也叫作activity_main.xml,然后在这个xml文件中编辑一个适用于平板的布局;重新运行程序可以看到系统会根据不同的屏幕大小选择对应的布局界面.

Android中常用的限定符:

**大小:**

| small  | 提供给小屏幕设备的资源   |
| ------ | ------------------------ |
| normal | 提供给中等屏幕设备的资源 |
| large  | 提供给大屏幕设备的资源   |
| xlarge | 提供给超大屏幕设备的资源 |

**分辨率:**

| ldpi   | 提供给低分辨率设备的资源(120dpi以下)     |
| ------ | ---------------------------------------- |
| mdpi   | 提供给中等分辨率设备的资源(120-160dpi)   |
| hdpi   | 提供给高分辨率设备的资源(160-240dpi)     |
| xhdpi  | 提供给超高分辨率设备的资源(240-320dpi)   |
| xxhdpi | 提供给超超高分辨率设备的资源(320-480dpi) |

**方向:**

| land | 提供给横屏设备的资源 |
| ---- | -------------------- |
| port | 提供给竖屏设备的资源 |

**(2)使用最小宽度限定符:**

与前面的layout-large类似,要想根据分辨率来设定布局,只需要将layout文件命名为形如layout-sw600dp即可.

## 21.广播机制

Android中的广播可以分为两类:

​	**标准广播:**一种完全异步执行的广播,在发出广播之后,所有的广播接收器几乎都会**在同一时刻接收到这条广播讯息**.这种广播的效率会比			较高,但也意味着它是**无法被拦截**的.

​	**有序广播:**一种同步执行的广播,在广播发出后**同一时刻只有一个广播接收器能够接收到这条广播讯息**,当这个广播接收器中的逻辑执行			完毕后,广播才会继续传递.所以**优先级高的广播接收器可以先接收到广播**并且**前面的接收器可以截断正在传递的广播**.

## 22.接收系统广播

(1)**动态注册监听网络变化**:

注册广播的方式一般有两种,一种是在代码中注册,也叫作动态注册,而另一种是在AndroidManifest.xml中注册,是静态注册;

下面是注册的方法和对网络变化进行监听的例子:

```java
public class MainActivity extends AppCompatActivity {

    private IntentFilter intentFilter;

    private NetworkChangeReceiver networkChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");		//想要监听什么广播,就要在这里添加相应的action
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, intentFilter);		//调用registerReceiver()方法进行注册,这样NetworkChangeReceiver就会收到所有值为android.net.conn.CONNECTIVITY_CHANGE的广播,也就可以监听网络的变化
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);		//动态注册的广播接收器一定要取消注册
    }

    class NetworkChangeReceiver extends BroadcastReceiver{

        @Override

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);		//通过getSystemService()方法得到了ConnectivityManager的实例,这是一个系统服务类,用于管理网络的;
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();		//调用ConnectivityManager的isAvailable()方法,可以判断出当前是否有网络了
            if(networkInfo != null && networkInfo.isAvailable()){
                Toast.makeText(context,"Network is available", Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(context, "Network is unavailable", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
```

另外,Android系统要求对一些对用户来说比较敏感的操作进行声明权限,在AndroidManifest.xml中添加:

```
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

(2)**静态注册实现开机启动**:

​	动态注册的广播接收器可以自由地注册与销毁,但是也存在着一个缺点,即必须要在程序启动后才能接收到广播,**因为注册的逻辑是写在onCreate()方法中的**.准备一条开机广播,当收到这条广播时就可以在onReceive()方法里执行相应的逻辑,从而实现开机启动的功能.可以使用AndroidStudio提供的快捷方式来创建一个广播接收器:右键com.example.broadcasttest包-->New-->Other-->Broadcast Receiver,在弹出的对话框里,**exported属性表示是否允许这个广播接收器接受本程序以外的广播**,**enabled属性表示是否启用这个广播接收器**.

​	值得注意的是,静态的广播接收器**一定要在AndroidManifest.xml文件中注册才可使用**,但是通过上面的方法创建,这一步会自动完成

```xml
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

可以看到,在<application>标签内出现了新的标签<receiver>,所有静态的广播接收器都是在这里进行注册的,与<activity>非常类似.需要对AndroidManifest.xml进行修改之后才能实现开机启动:

```xml
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/>
...
    <receiver
        android:name=".BootCompleteReceiver"
        android:enabled="true"
        android:exported="true">
        <intent-filter>
            <action android:name="android.intent.action.BOOT_COMPLETED"/>
        </intent-filter>
    </receiver>
...
```

## 23.发送自定义广播

(1)**发送标准广播**:

首先要先定义一个广播接收器来准备接受此广播才行.

新建一个接收器类继承自BroadcastReceiver,然后再AndroidManifest中添加<receiver>标签;之后可以在活动中注册一个按钮,再在活动中修改onCreate()方法中的关于按钮的活动:

```java
Button button = (Button) findViewById(R.id.button);
button.setOnClickListener(v -> {
    Intent intent = new Intent("com.example.broadcasttest.MY_BROADCAST");
    sendBroadcast(intent);
});
```

由于广播是是用Intent进行传递的,因此还可以在Intent中携带一些数据传递给广播接收器.

## 24.使用本地广播

## 25.广播--强制下线功能

## 26.文件储存

(1)**将数据存储到文件中**:

​	Context类中提供了一个openFileOutput()方法,可以**用于将数据存储到指定的文件中**.这个方法的第一个参数是文件名,注意不能包含路径;第二个参数是文件的操作模式,主要有两种模式可以选择,**MODE_PRIVATE**和**MODE_APPEND**:其中MODE_PRIVATE是默认的操作模式,表示当指定同名的时候,所写的内容**会覆盖原文件中的内容**;而MODE_APPEND表示如果已经存在,则**向其中追加内容**,不存在就新建文件

以下为一个具体的例子

```java
public class MainActivity extends AppCompatActivity {

    private EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        edit = (EditText) findViewById(R.id.edit);
        Button destroyButton = (Button) findViewById(R.id.destroy_button);
        destroyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String inputText = edit.getText().toString();
        save(inputText);		//调用save()方法将输入的内容保存到inputText中
    }

    public void save(String inputText){
        FileOutputStream out = null;
        BufferedWriter writer = null;		//声明了两个变量out和writer,分别用于文件输出流和缓冲写入器.初始值都设null.
        try {	//使用try-catch块来捕获可能发生的IOException异常.IOException是一个常见的异常类型,用于处理输入输出操作中可能出现的错误.
            out = openFileOutput("data", Context.MODE_PRIVATE);//调用openFileOutput方法打开一个名为data的文件,并以私有模式(Context.MODE_PRIVATE)进行写入.
            writer = new BufferedWriter(new OutputStreamWriter(out));//BufferedWriter提供了缓冲功能,可以提高写入效率
            writer.write(inputText);
        }catch ( IOException e){
            e.printStackTrace();
        }finally {		//尝试关闭BufferedWriter对象,确保资源得到正确释放
            try{
                if (writer != null){
                    writer.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
```

销毁程序后可以通过以下路径找到输出的data文件:View--tool Windows--Device Explorer.

(2)**从文件中读取数据**:

类似于将数据存储到文件中,Context类中还提供了一个openFileInput()方法,用于从文件中读取数据.这个方法只接受一个参数,即要读取的文件的文件名,并返回一个FileInputStream对象.

```java
public class MainActivity extends AppCompatActivity {

    private EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ...
        String inputText = load();
        if( !TextUtils.isEmpty(inputText)){
            edit.setText(inputText);
            edit.setSelection(inputText.length());
            Toast.makeText(this, "Restoring succeeded", Toast.LENGTH_SHORT).show();
        }
    }
	...
    public String load(){
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            in = openFileInput("data");
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine())!=null){
                content.append(line);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(reader !=null){
                try {
                    reader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        return content.toString();
    }
}
```

这样当销毁活动后再次打开时,EditText中已经自动填入了上一次输入的内容.

## 27.SharedPreferences存储

(1**)将数据存储到SharePreferences中**:

​	使用SharePreferences存储数据首先要获取到SharePreferences对象,这里有3种方法用于得到SharePreferences对象:

​		**a.Context类中的getSharePreferences()方法**:这个方法接收两个参数,第一个参数用于指定SharePreferences文件的名称,若指定的名称不存在则会创建一个,SharePreferences文件都是存放在/data/data/<package name>/shared_prefs/目录下的.第二个参数用于指定操作模式,MODE_PRIVATE模式和传入0的效果是相同的,表示只有当前的应用程序才可以对这个SharePreferences文件进行读写.MODE_APPEND是追加模式,以该模式获取SharePreferences并提交数据时,不会覆盖原有的数据而是在其后继续添加.

​		**b.Activity类中的getSharePreferences()方法**:这个方法和Context中的getSharePreferences()方法相似,但是只接收一个操作模式参数,使用这个方法时会自动将当前活动的类名作为SharePreferences的文件名.

​		**c.PreferenceManager类中的getDefaultSharePreferences()方法**:这是一个静态方法,接受一个Context参数,并自动使用当前应用程序的包名作为前缀来命名SharePreferences文件.

​	在得到SharePreferences对象之后,便可以向SharePreferences文件中存储数据了,主要分为3步进行实现:

​		**a.**调用SharePreferences对象的edit()方法来获取一个SharePreferences.Edittor对象

​		**b.**向SharePreferences.Editor对象中添加数据,比如添加一个字符串,则使用putString()方法.

​		**c.**调用apply()方法将添加的数据提交,从而完成数据存储操作.

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_main);
    Button saveData = (Button) findViewById(R.id.save_data);
    saveData.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();	//获取一个SharedPreferences.Editor对象
            editor.putString("name", "EV4N");
            editor.putInt("age", 18);
            editor.putBoolean("married", false);	//写入data的内容
            editor.apply();		//提交添加的数据
        }
    });
}
```

(2)**从SharePreferences中读取数据**：

```java
protected void init(){
    Button restoreData = (Button) findViewById(R.id.restore_data);
    restoreData.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SharedPreferences pref= getSharedPreferences("data" ,MODE_PRIVATE);		//通过getSharedPreferences()方法得到了SharedPreferences对象
            String name = pref.getString("name","");
            int age = pref.getInt("age",0);
            boolean married = pref.getBoolean("married", false);	//调用SharedPreferences的各个get方法获取存储的信息
            Log.d("MainActivity", "name is "+name);
            Log.d("MainActivity","age is "+age);
            Log.d("MainActivity", "married is "+ married);		//打印读取的信息
        }
    });
}
```

(3)**实现记住密码的功能**：

先编辑是否记住密码的选项布局:

```xml
<LinearLayout
    android:orientation="horizontal"
    android:layout_width="match_parent"
    android:layout_height="wrap_content">
    <CheckBox
        android:id="@+id/remember_pass"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"/>	//CheckBox的作用是提供一个可勾选的框

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="18sp"
        android:text="Remember Password"/>
</LinearLayout>
```

```java
public class LoginActivity extends BaseActivity{

    ...

    private SharedPreferences pref;

    private SharedPreferences.Editor editor;

    private CheckBox rememberPass;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref = PreferenceManager.getDefaultSharedPreferences(this);		//获取当前Activity对应的共享偏好设置并赋值给pref
        rememberPass = (CheckBox) findViewById(R.id.remember_pass);
        boolean isRemember = pref.getBoolean("remember_password", false);
        if(isRemember){
            String account = pref.getString("account", "");
            String password = pref.getString("remember_password", "");		//获取pref对象的String对象
            accountEdit.setText(account);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }
        accountEdit = (EditText) findViewById(R.id.account);
        passwordEdit = (EditText) findViewById(R.id.password);
        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = accountEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                if(account.equals("admin")&&password.equals("666666")){
                    editor= pref.edit();
                    if(rememberPass.isChecked()){
                        editor.putBoolean("remember_password", true);
                        editor.putString("account", account);
                        editor.putString("password", password);
                    }else{
                        editor.clear();
                    }
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(LoginActivity.this, "account or password is invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
```

## 28.SQLite数据库存储

(1)**创建数据库**

SOLite是Android系统内置的一个数据库,Android中专门提供了一个SQLiteOpenHelper帮助类.

SQLiteOpenHelper是一个抽象类,所以如果想要使用它就需要创建一个帮助类去继承它.其中SQLiteOpenHelper中有两个抽象方法,分别是onCreate()和onUpgrade(),要在帮助类中重写这两个方法去实现创建,升级数据库的逻辑.

SOLiteOpenHelper方法中还有两个非常重要的实例方法,分别是getReadableDatabase()和getWritableDatabase().这两个方法都可以创建或打开一个现有的数据库(如果数据库已经存在则直接打开,否则建立一个新的数据库),并返回一个可对数据库进行读写操作的对象.但是当数据库不可写入时,getReadableDatabase()方法返回的对象将以只读的方式打开数据库有,而getWritableDatabase()则会出现异常.

下面来具体使用以下SQLiteOpenHelper:

创建数据库需要用到建表语句,例如:

``````java
create table Book(
	id integer primary key autoincrement,
	author text,
	price real,
	pages integer,
	name text)
``````

以上是Book的建表语句,但是要在代码中执行这条SQL语句才能完成创建表的操作:

```java
public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_BOOK = "create table Book("
            + "id integer primary key autoincrement,"
            + "author text,"
            + "price real,"
            + "pages integer,"
            + "name text)";				//将建表语句定义成了一个字符串常量

    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){		//这是一个SQLiteOpenHelper的构造方法,传入4个参数:Context, 数据库名, 查询数据的时候返回的一个自定义的Cursor,一般传入null, 当前数据库的版本号,可用于对数据库进行升级操作
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK);		//调用SQLiteDatabase的execSQL()方法执行上面的建表语句,保证在建立数据库的同时成功创建Book表 
        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
```

接下来编辑MainActivity中的代码:

```java
public class MainActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 1);	//前面提到的四个参数
        Button createDatabase = (Button) findViewById(R.id.create_database);
        createDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.getWritableDatabase();	//创建一个数据库
            }
        });
    }
}
```

此时运行程序,BookStore.db数据可和Book表都已经创建成功了.可以通过Device Explorer查看现存的数据库:

![image-20250403230004123](C:\Users\Akinass\AppData\Roaming\Typora\typora-user-images\image-20250403230004123.png)

但是Book表是无法通过Device Explorer查看到的,因此可以通过adb shell来对数据库和表的创建情况进行检查.adb是Android SDK自带的一个调试工具.

![image-20250403231159969](C:\Users\Akinass\AppData\Roaming\Typora\typora-user-images\image-20250403231159969.png)

第一个文件是创建好的BookStore数据库,而第二个文件是为了让数据库能够支持事务而产生的临时日志文件.接着可以用sqlite命令打开数据库,输入sqlite3并在后面接上要查看的数据库的名字就可以打开数据库了.接着键入.table命令可以看到里面的表.最后还可以通过.schema命令查看建表语句

(2)**升级数据库**:

MyDatabaseHelper中还有一个onUpgrade()方法用于**对数据库进行升级**.

下面是一个例子,再添加一张表Category.

按照之前建立Book表的方法建立一个Category表:建立一个静态表;在onCreate()方法中db一个新的表CREATE_CATEGORY

**接着**在onUpgrade()方法中更新如下代码:

```java
@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("drop table if exists Book");
    db.execSQL("drop table if exists Category");
    onCreate(db);
}
```

这段代码中执行了两条DROP语句,表示只要发现数据库中存在Book和Category两张表,就将表删除,然后再创建两张表;因为如果在创建的时候发现表已经存在,则会直接报错.

接下来是执行onUpgrade()方法,之前所说的SQLiteOpenHelper的构造方法里面包含四个参数,其中第四个表示的是版本号,也就是说要想更新数据库,只需传入一个比1大的数即可:

```java
dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 2);		//第四个参数发生变化
```

打开shell就可以查看新建的表了.

(3)**添加数据**:

对数据的操作有4种(CRUD): C代表Create, R代表Retrieve, U代表Update, D代表Delete,.

首先是向数据库中添加数据.SQLiteDatabase中提供了一个insert()方法,这个方法是专门用于添加数据的,接收3个参数,第一个参数是表名,第二个参数用于在未指定添加数据的情况下给某些可为空的列自动赋值NULL,传入null即可;第三个参数是一个ContentValues对象,用于向ContentValues中添加数据,只需要将表中的每个列名以及相应的数据传入即可.

```java
Button addData =(Button) findViewById(R.id.add_data);
addData.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();		//先获取SQLiteOpenHelper对象
        ContentValues values = new ContentValues();		//再使用ContentValues来对需要添加的数据进行组装
        //开始装入第一条数据
        values.put("name", "The Da Vinci Code");
        values.put("author", "Dan Brown");
        values.put("pages", 454);
        values.put("price", 16.96);
        db.insert("Book",null, values);		//调用insert()方法将数据添加到表当中
        values.clear();
        //开始装入第二条数据
        values.put("name", "The Lost Symbol");
        values.put("author", "Dan Brown");
        values.put("pages", 510);
        values.put("price", 19.95);
        db.insert("Book", null, values);
    }
});
```

在shell中输入SQL查询语句select * from Book;就可以查看数据了.

(4)**更新数据**:

SQLiteDtabase中提供了一个update()方法,用于对数据进行更新,这个方法接收4个参数:第一个参数是表名;第二个参数是ContentValues对象,要把更新数据在这里组装进去;第三,第四个参数用于约束更新某一行或某几行中的数据,不指定的话默认就是更新所有行.

```java
Button dataUpdate = (Button) findViewById(R.id.update_data);
dataUpdate.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();		//获取SQLiteOpenHelper对象
        ContentValues values = new ContentValues();		
        values.put("price", 10.99);		//需要更新的内容
        db.update("Book", values,"name= ?", new String[] {"The Da Vinci Code"});	//更新name等于?的行,?是占位符
    }
});
```

这样重新打开Book表时可以看到所有名为"The Da Vinci Code"的name变元对应的行的price变量都变成了10.99.

(5)**删除数据**:

SQLiteDtabase中提供了一个delete()方法,用于删除数据.这个方法接收3个参数:第一个参数是表名,第二个,第三个参数用于约束删除某一行或某几行的数据,不指定的话默认就是删除所有行.

```java
Button dataDelete = (Button) findViewById(R.id.delete_data);
dataDelete.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("Book", "pages > ?", new String[] {"500"});
    }
});
```

(6)**查询数据**:

SQLiteDatabase中还提供了一个query()方法用于对数据进行查询.这个方法的参数**非常复杂**,最短的一个方法重载也需要传入7个参数:

第一个参数是表名;第二个参数用于指定去查询哪几列,如果不指定就是默认查询所有列;第三个,第四个参数用于约束查询某一行或某几行的数据,不指定则默认查询所有行的数据;第五个参数用于指定需要去group by的列,不指定则表示不对查询结果进行group by操作;第六个参数用于对group by之后的数据进行进一步的过滤,不指定则表示不进行过滤;第七个参数用于指定查询结果的排序方式,不指定则表示使用默认的排序方式.

| query()方法参数 | 对应SQL部分               | 描述                          |
| --------------- | ------------------------- | ----------------------------- |
| table           | from table_name           | 指定查询的表名                |
| columns         | select column1, column2   | 指定查询的列名                |
| selection       | where column = value      | 指定where的约束条件           |
| selectionArgs   | -                         | 为where中的占位符提供具体的值 |
| groupBy         | group by column           | 指定需要group by的列          |
| having          | having column = value     | 对group by后的结果进一步约束  |
| orderBy         | order by column1, column2 | 指定查询结果的排序方式        |

调用query()方法后会返回一个Cursor对象,查询到的所有数据都将从这个对象中取出.

```java
Button dataQuery = (Button) findViewById(R.id.query_data);
dataQuery.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //查询Book表中的所有数据
        Cursor cursor = db.query("Book", null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String author = cursor.getString(cursor.getColumnIndex("author"));
                int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                double price = cursor.getDouble(cursor.getColumnIndex("price"));
                Log.d("MainActivity", "book name is "+ name);		//将获取到的数据打印在日志里
                Log.d("MainActivity", "book author is "+ author);
                Log.d("MainActivity", "book pages is "+ pages);
                Log.d("MainActivity", "book price is "+ price);
            }while (cursor.moveToNext());
        }
        cursor.close();
    }
});
```

moveToFirst()方法将数据的指针移动到第一行的位置,接着遍历查询到的每一行数据.在这个循环中可以通过Cursor的getColumnIndex()方法获取到某一列在表中对应的位置索引,然后将这个索引传入到相应的取值方法中,就可以得到从数据库中读取到的数据了.最后还要调用close()方法关闭Cursor

### 知晓当前是在哪一个活动(Kotlin)

在Kotlin中，javaClass表示获取当前实例的Class对象，与之前Java中调用的getClass()方法时一致的；而在Kotlin中Activity::class.java表示获取Activity类的Class对象，相当于Java中调用Activity.class

### 改写startActivity()方法

使用一般的intent传递重要参数时，通常会采用以下的形式

```kotlin
val intent = Intent(this, Activity::class.java)
intent.putExtra("param1", "data1")
intent.putExtra("param2", "data2")
startActivity(intent)
```
但这个传递方式不能清楚需要传递哪些数据，因此可以在目标Activity中增添

```kotlin
class SecondActivity : BaseActivity() {
    ...
    companion object {
        fun actionStart(context: Context, data1:String, data2:String) {
            val intent = Intent(context, SecondActivity::class.java)
            intent.putExtra("param1", data1)
            intent.putExtra("param2", data2)
            context.startActivity(intent)
        }
    }
}
```

这段代码中的companion object中定义的方法可以像Java中的静态方法那样调用，其中的actionStart()方法需要三个参数，分别是活动和两个参数（自定义），这样一来我们可以将原本的传递方法改写成以下的形式

```kotlin
button1.setOnClickListener{
   SecondActivity.actionStart(this,"data1", "data2")
}
```

这样便可以清晰地看到传输到SecondActivity的重要信息

## WebView

WebView用于在应用内打开网页而不是使用intent打开系统浏览器。

```kotlin
val binding = ActivityMainBinding.inflate(layoutInflater)
setContentView(binding.root)
binding.webView.settings.javaScriptEnabled=true	//getSetting()方法设置支持JS脚本
binding.webView.webViewClient = WebViewClient()	//当需要从一个网页跳转到另一个网页时，目标网页仍然在当前WebView中显示
binding.webView.loadUrl("https://www.jd.com")	//传入网址
```

由于程序使用到了网络功能，因此要在AndroidManifest.xml文件中加入权限声明

```kotlin
<uses-permission android:name="android.permission.INTERNET" />
```

## OkHttp

OkHttp的具体用法：

```kotlin
val client = OkHttpClient()	//创建一个OkHttpClient的实例
val request = Request.Builder().url("https://www.baidu.com").build()	//创建一个Request对象
val response = client.newCall(request).execute()	//创建一个Call对象，并用execute()方法发送请求并获取服务器返回的数据
val responseData = response.body?.string()	//得到返回的具体内容
if (responseData != null) {
    showResponse(responseData)	//将返回的数据显示在界面上
}
```

## Gson

GSON开源库能够在很大程度上简化解析JSON数据，可以将一段JSON格式的字符串自动映射成一个对象

首先在dependencies中添加

```kotlin
implementation("com.google.code.gson:gson:版本")
```

先定义一个类并加入json对应的字段如

![image-20250714113216843](C:\Users\Akinass\AppData\Roaming\Typora\typora-user-images\image-20250714113216843.png)

```kotlin
class App(val id: String, val name: String, val version: String)
```

上面的json文件中需要解析的是HSON数组，要借助TypeToken将期望解析成的数据类型传入fromJson()方法中

```kotlin
val typeOf = object : TypeToken<List<Game>>() {}.type
val games = gson.fromJson<List<Game>>(jsonData, typeOf)
```

## Retrofit

Retrofit是Square公司在OkHttp的基础上进一步开发出来的应用层网络通信库。Retrofit将功能同属一类的服务器接口定义到同一个接口文件中，让代码结构变得更加合理。而使用Retrofit时也不用关心网络通信的细节，只需要在接口文件中声明一系列方法和返回值，然后通过注解的方式指定该方法对应哪个服务器接口，以及需要提供哪些参数。然后在程序中调用该方法时，Retrofit就会自动相对应的服务器接口发起请求，并将响应的数据解析成返回值声明的类型。

### 1.Retrofit的基本用法

Retrofit会借助GSON将JSON数据转换成对象，因此同样**需要一个App类**

```kotlin
class App(val id: String, val name: String, val version: String)
```

然后就可以根据服务器接口的功能进行归类，创建不用种类的接口文件

```kotlin
interface AppService {
    
    @GET("get_data.json")	//@GET注解表示放调用getAppData()方法时会发起一条GET请求
    fun getAppData(): Call<List<App>>	//getAppData()方法的返回值必须声明成Retrofit中内置的Call类型，并通过泛型来指定服务器响应的对象是一个List。
}
```

```kotlin
binding.getAppDataBtn.setOnClickListener {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/") 	//指定所有Retrofit请求的根路径
        .addConverterFactory(GsonConverterFactory.create())		//指定Retrofit在解析数据时所使用的转换库
        .build()
    val appService = retrofit.create(AppService::class.java)	//调用retrofit对象的create方法并传入具体Service接口所对应的Class类型创建一个该接口的动态代理对象
    appService.getAppData().enqueue(object : Callback<List<App>> {	//getAppData()方法返回一个Call<List<App>>对象，再调用它的enqueue()方法,Retrofit就会根据注解中配置的服务器接口地址进行网络请求，接着服务器响应的数据会会掉到enqueue()方法中传入的Callback实现里面
        override fun onResponse(call: Call<List<App>>,
            response: Response<List<App>>) {
            val list = response.body()	//调用reponse.body()方法会得到Retrofit解析后的对象，也就是List<App>类型的数据
            if (list != null) {
                for (app in list) {
                    Log.d("MainActivity" , "id is ${app.id}")
                    Log.d("MainActivity" , "name is ${app.name}")
                    Log.d("MainActivity" , "version is ${app.version}")
                }
            }
        }

        override fun onFailure(call: Call<List<App>>, t: Throwable) {
            t.printStackTrace()
        }
    })
}
```

注意的是，当发起请求的时候，Retrofit会**自动在内部开启子线程**，当数据回调到Callback中之后，Retrofit又会**自动切换回主线程**，因此整个过程中我们都不需要考虑线程切换问题。

### 2.处理复杂的接口地址类型

在很多场景下，接口地址中的部分内容可能会是动态变化的，例如

```http
GET http://example.com/<page>/get_data.json
```

在这个接口中<page>部分代表页数，服务器返回的数据会随着我们传入不同的页数而变化，可以用下面的方式写

```kotlin
interface ExampleService {
    
    @GET("{page}/get_data.json")	
    fun getData(@Path("page") page: Int): Call<Data>	
    //使用{page}占位符并在getData()方法中添加一个page参数，并使用@Path("page")注解来声明这个参数，这样当调用getData()方法发起请求时，Retrofit就会自动将page参数的值替换到占位符的位置，组成合法的请求地址
}
```

另外，很多服务器接口还会要求我们传入一系列的参数，格式如下

```http
GET http://example.com/get_data.json?u=<user>&t=<token>
```

接口地址最后是哟个问号连接参数部分，每个参数都是一个使用等号连接的键值对，多和参数之间使用&符号进行分隔，而在Retrofit中针对这种带参数的GET请求专门提供了一种语法支持：

```kotlin
interface ExampleService {
    
    @GET("get_data.json")
    fun getData(@Query("u") user: String, @Query("t") token: String): Call<Data>
}
```

getData()方法中添加了user和token这两个参数，并使用@Query注解对它们进行声明，这样当发起网络请求的时候，Retrofit就会自动按照带参数GET请求的格式将这两个参数构建到请求地址当中。

POST、PUT、PATCH、DELETE这几种请求类型与GET请求不同，他们更多是用于操作服务器上的数据而不是获取服务器上的数据，所以通常他们对于服务器相应的数据并不关心。这个时候就可以使用ResponseBody，表示Retrofit**能够接收任意类型的响应数据**，并且**不会对响应数据进行解析**。

**例子1：删除一条数据**

接口地址：

DELETE http://example.com/data/<id>

使用@DELETE请求删除数据，@Path注解动态指定id

```kotlin
interface ExampleService {
    @DELETE("data/{id}")
    fun deleteData(@Path("id") id: String): Call<ResponseBody>
}
```

**例子2：向服务器提交数据**

接口地址：

POST http://example.com/data/create

{"id": 1, "content": "The description for this data."}

需要使用POST请求来提交数据，需要将数据放到HTTP请求的body部分，用@Body注解实现：

```kotlin
interface ExampleService {
    @POST("data/create")
    fun createData(@Body data: Data): Call<ResponseBody>
}
```

**例子3：在HTTP请求的header中指定参数**

接口地址：

GET http://example.com/get_data.json

User-Agent: okhttp

Cache-Control: max-age=0

这些header参数其实就是一个个的键值对，可以在Retrofit中直接使用@Headers注解来对它们进行声明

```kotlin
interface ExampleService {
    @Headers("User-Agent: okhttp", "Cache-Control: max-age=0")
    @GET("get_data.json")
    fun getData(): Call<Data>
}
```

但是这种写法只能进行静态header声明，如果想要动态指定header的值，就需要使用@Header注解

```kotlin
interface ExampleService {
    @GET("get_data.json")
    fun getData(@Header("User-Agent") userAgent: String,
               @Header("Cache-Control") cacheControl: String): Call<Data>
}
```

### 3.Retrofit构建器的最佳写法

 有时我们需要调用很多服务器接口，这是可以将创建Retrofit对象这部分封装起来，从而简化获取Service接口动态代理对象的过程。

```kotlin
object ServiceCreator {
    
    private const val BASE_URL = "http://10.0.2.2/"
    
    private val retrofit = Retrofit.Builder()
   		.baseUrl(BASE_URL)
    	.addConverterFactory(GsonConverterFactory.create())
    	.build()
    
    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
}
```

这段代码提供了一个外部可见的create()方法，并接收一个Class类型的参数，这样当我们想获取一个AppService接口的动态代理对象时，只需这样写

```kotlin
val appService = ServiceCreator.create(AppService::class.java)
```

利用泛型实话还可以优化ServiceCreator中的代码

```kotlin
object ServiceCreator {
	...
    inline fun <reified T> create(): T = create(T::class.java)
}
```

这样获取AppService接口的动态代理对象

```kotlin
val appService = ServiceCreator.create<AppService>()
```

## JetPack

### 1.ViewModel

ViewModel是专门用于存放于界面相关的数据的，也就是说只要是界面上能看得到的数据，它的相关变量都应该存放在ViewModel中，而不是Activity中。

另外，ViewModel还有一个重要的特性，就是它的生命周期不同于Activity，只有当Activity退出的时候才会跟着Activity一起销毁，所以当旋转手机屏幕时，界面上显示的数据也不会丢失。

使用ViewModel之前要先再gradle文件中添加依赖：

```kotlin
implementation("com.androidx.lifecycle:lifecycle-extensions:2.2.0")
```

通常来讲，编程规范要求给每一个Activity和Fragment都创建一个对应的ViewModel，因此我们创建对应的ViewModel时要继承自ViewModel

```kotlin
class MainViewModel : ViewModel() {
    ...	//存放界面上显示出来的数据
}
```

这样一来在Activity中编写逻辑时要先定义一个ViewModel的实例，一般先延迟定义。接着在函数中创建实例时一定是要用ViewModelProvider来获取ViewModel的实例

```kotlin
ViewModelProvider(<Activity或Fragment实例>).get(<自订的ViewModel>::class.java)
```

这么写的原因在于ViewModel有独立的生命周期，要长于Activity，所以如果在onCreate()方法中创建ViewModel的实例，那每次旋转手机屏幕onCreate()方法执行时就会创建一个新的ViewModel实例，也就无法保留界面数据了。

**向ViewModel传递参数：**

如果想要向ViewModel中传入参数（比如计数器在中断之后仍想从原来的节点开始计数），此时可以这样写

```kotlin
class MainViewModelFactory(private val countReserved: Int): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(countReserved) as T
    }
}
```

```kotlin
class MainViewModel(countReserved: Int) : ViewModel() {

    var counter = countReserved
}
```

之后再在Activity中定义countReserved并在Activity实例的位置填入Factory即可

### 2.Lifecycles

如果想要在一个非Activity的类中去感知Activity的生命周期，就可以使用Lifecycles组件。新建一个Myobserver类并让它实现LifecycleObserver接口

```kotlin
class MyObserver : LifecycleObserver {
    ...
}
```

接下来在MyObserver中定义方法感知Activity的生命周期

```kotlin
class MyObserver : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun activityStart() {
        Log.d("MyObserver", "activityStart")
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun activityStop() {
        Log.d("MyObserver", "activityStop")
    }
}
```

这里使用了@OnLifecycleEvent注解，并传入了一种生命周期事件。生命周期事件的类型一共有7种：ON_CREATE、ON_START、ON_RESUME、ON_PAUSE、ON_STOP和ON_DESTROY分别对应Activity的生命周期回调，还有一种是ON_ANY，表示可以匹配Activity的任何生命周期回调。

接下来在Activity的onCreate()方法里调用addObserver方法并传入MyObserver方法即可

```kotlin
lifecycle.addObserver(MyObserver())
```

### 3.LiveData

**基本用法：**

LiveData可以包含任何类型的数据，并且在数据发生变化的时候通知给观察者。

MutableLiveData对象：一种可变的LiveData，主要有三种读写数据的方法，分别是getValue()、setValue()和postValue()

有推荐的做法是永远只暴露不可变的LiveData给外部。这样在非ViewModel中就只能观察LiveData的数据变化而不能给LiveData设置数据

```kotlin
class MainViewModel(countReserved: Int) : ViewModel() {

    val counter: LiveData<Int>
        get() = _counter

    private val _counter = MutableLiveData<Int>()

    init {
        _counter.value = countReserved
    }

    fun plusOne() {
        val count = _counter.value ?: 0
        _counter.value = count + 1
    }

    fun clear() {
        _counter.value = 0
    }
}
```

这样设置 _counter变量并给它加上private修饰符之后 _counter对于外部就是不可见的，之后再定义一个counter变量且类型为不可变的LiveData。这样一来当外界调用counter变量时，实际上获得的是 _counter的实例，但是无法给它设置数据，保证了ViewModel的数据封装性。

**map和switchMap：**

map的用法就是将一个类型的LiveData自由地转型成任意其他类型的LivaData

假设有这样一个类，这个类中包含用户的姓名和年龄，但Activity中只显示用户的姓名，所以如果还是直接将LivaData展示给外部，就会泄露额外信息，这时就可以**使用map()方法将原本的LivaData转化为只包含用户姓名的LiveData**

```kotlin
data class User(var name: String, var age: Int)
```

```kotlin
class MainViewModel(countReserved: Int) : ViewModel() {
    private val userLiveData = MutableLivaData<User>()	//private保证数据的封装性
    val userName: LiveData<String> = Transformation.map(userLiveData) { user ->
  		"${user.name}"
    }
}
```

这里调用了Transformation的map()方法来对LiveData的数据类型进行转换。map()方法接收两个参数，第一个参数是原本的LiveData对象，第二个参数是一个转换函数，函数的逻辑就是把User对象转换成一个只包含用户姓名的字符串。

如果ViewModel中的某个LiveData对象是调用另外的方法获取的，那么可以借助SwitchMap()方法**将这个LiveData对象转换成另外一个可观察的LiveData对象**

```kotlin
private val userIdLiveData = MutableLiveData<String>()

val user: LiveData<User> = userIdLiveData.switchMap { userId ->
    Repository.getUser(userId)	//getUser()方法返回另外一个LiveData对象
}

fun getUser(userId: String){
    userIdLiveData.value = userId
}
```

switchMap()方法同样接受两个参数，第一个参数传入的是新增的userIdLiveData，switchMap()方法会对他进行观察，第二个参数是一个转换函数，**必须在这个转换函数中返回一个LiveData对象**

有些时候ViewModel中某个获取数据的方法可能是内有参数的，这个时候就将刚刚的MutableLiveData的泛型设置为Any？即可。

总而言之，LiveData与ViewModel结合使用，但其实也与Lifecycle有着非常紧密的关系，当Activity被遮盖时，LiveData中的数据变化是不会通知给观察者的，只有当Activity重新恢复可见状态时才会将数据通知给观察者。且Livedata的最新的那部分数据回传给重新变成可见状态的Activity，其他的数据会被直接丢弃。

### 4.Room

#### （一）使用Room进行增删改查

Room主要由Entity、Dao和Database这3部分组成:

​	**Entity**：用于封装实际数据的实体类，每个实体类都会在数据库中有一张对应的表，表中的列是根据实体类		       的字段自动生成的

​	**Dao**：Dao是数据访问对象的意思，通常会在这里对数据库的各项操作进行封装，在实际编程的时候，逻辑层		   直接和Dao层交互即可

​	**Database**：用于定义数据库中的关键信息，包括数据库的版本号、包含哪些实体类以及提供Dao层的访问实			      例

```kotlin
@Entity
data class User(var firstName: String, var lastName: String, var age: Int) {
    
    @PrimaryKey(autoGenerate = true)    //@PrimaryKey注解将id设置为主键，autoGenerate = true表示主键自增
    var id: Long = 0
}
```

修改User类之后的代码如上

接着新建一个userDao接口

```kotlin
@Dao
interface UserDao {

    @Insert
    fun insertUser(user: User): Long

    @Update
    fun updateUser(newUser: User)

    @Query("select * from User")
    fun loadAllUsers(): List<User>

    @Query("select * from User where age > :age")
    fun loadUsersOlderThan(age: Int): List<User>

    @Delete
    fun deleteUser(user: User)

    @Query("delete from User where lastName = :lastName")
    fun deleteUserByLastName(lastName: String): Int
}
```

在这个接口类中，要现在接口名称上使用一个@Dao注解，这样Room才能将它识别成一个Dao，然后在每个增删改查的方法前加上对应的注解@Insert、@Delete、@Update和@Query，使用前三个方法时只需传入参数，但是使用查询方法时Room无法知道我们想要查询哪些数据，因此**必须在@Query注解中编写具体的SQL语句**

最后一步就是定义Database了，只需要定义数据库的版本号、包含哪些实体类以及提供Dao层的访问实例。

```kotlin
@Database(version = 1, entities = [User::class])
abstract class AppDatabase : RoomDatabase() {	//AppDatabase一定要继承自RoomDatabase类，并且一定要声明为抽象类和提供抽象方法，用于获取之前编写的Dao实例

    abstract fun userDao(): UserDao

    companion object {	//使用单例模式是因为原则上全局应该只存在一份AppDatabase的实例

        private var instance: AppDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): AppDatabase {
            instance?.let {
                return it	//如果instance的变量不为空就直接返回，否则调用Room.databaseBuilder()方法来构建一个AppDatabase的实例
            }
            return Room.databaseBuilder(context.applicationContext,
                AppDatabase::class.java, "app_database")	//databaseBuilder()方法接收三个参数，第一个参数一定要使用applicationContext，第二个参数是AppDatabase的Class类型，第三个参数是数据库名
                .build().apply {	//用build()方法完成构建并将创建出来的实例赋值给instance变量
                instance = this
                }
        }
    }
    
}
```

在AppDatabase类的头部使用@Database注解并在注解中声明了数据库的版本号以及包含哪些实体类，多个实体类之间用逗号隔开。

最后再在Activity中获取UserDao的实例，创建对象然后编辑增删改查的事件逻辑。

#### （二）Room的数据库升级

如果现在想要在数据库中添加一张Book表，那么就要先创建一个Book的实体类

```kotlin
@Entity
data class Book(var name: String, var pages: Int, var author: String) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
```

用类似的方法构造Book实体类，包含主键id、书名、页数和作者这几个字段。

接着再创建一个BookDao接口

```kotlin
@Dao
interface BookDao {

    @Insert
    fun insertBook(book: Book): Long

    @Query("select * from Book")
    fun loadAllBooks(): List<Book>
}	//随意定义一些API
```

接下来是最为关键的一步

```kotlin
@Database(version = 2, entities = [User::class, Book::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun bookDao(): BookDao

    companion object {

        val MIGRATION_1_2 = object : Migration(1,2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("create table Book (id integer primary " +
                        "key autoincrement not null, name text not null, pages integer not null)")
            }
        }
        private var instance: AppDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): AppDatabase {
            instance?.let {
                return it
            }
            return Room.databaseBuilder(context.applicationContext,
                AppDatabase::class.java, "app_database")
                .addMigrations(MIGRATION_1_2)
                .build().apply {
                    instance = this
                }
        }
    }

}
```

首先在@Database注解中将版本号升级为2，并将Book类添加到实体类声明中，接着又提供了一个bookDao()方法用于获取BookDao实例

接着实现一个Migration匿名类并传入1和2这两个参数，表示当数据库版本从1升级到2是就执行这个匿名类中的升级逻辑。由于要新增一张表，所以需要在migrate方法中编写相应的建表语句，而且Book表的**建表语句必须和Book实体类中声明的结构完全一致**。

最后在构建AppDatabase实例的时候，加入一个addMigrations()方法，并把MIGRATION_1_2传入即可。

不过，每次数据库升级并不一定都需要新增一张表，也可以是想原有的表中添加新的列，这种情况只需要使用alter语句修改表结构就可以了

```kotlin
@Database(version = 3, entities = [User::class, Book::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun bookDao(): BookDao

    companion object {

        val MIGRATION_2_3 = object : Migration(2,3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("alter table Book add column author text not null default 'unknown'")
            }
        }
        private var instance: AppDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): AppDatabase {
            instance?.let {
                return it
            }
            return Room.databaseBuilder(context.applicationContext,
                AppDatabase::class.java, "app_database")
                .addMigrations(MIGRATION_1_2.MIGRATION_2_3)
                .build().apply {
                    instance = this
                }
        }
    }

}
```

### 5.WorkManager

由于Android的频繁的功能和API变更，要保证应用程序在不同系统版本上的兼容性，Google就推出了WorkManager组件。WorkManager用于处理一些要求定时执行的任务，它可以根据操作系统的版本自动选择底层是使用AlarmManager实现还是JobScheduler实现，从而降低使用成本。其次它还支持周期性任务、链式任务处理等功能。

#### （一）WorkManager的基本用法

（添加库：“androidx.work:work-runtime:”）

WorkManager的用法主要分为以下三步：

（1）定义一个后台任务，并实现具体的任务逻辑；

（2）配置该后台人物的运行条件和约束信息，并构建后台任务请求；

（3）将该后台任务请求传入WorkManager的enqueue()方法中，系统会在合适的时间运行。

```kotlin
class SimpleWorker(context: Context, params: WorkerParameters) : Worker(context,params) {

    override fun doWork(): Result {
        Log.d("SimpleWorker", "do work in SimpleWorker")
        return Result.success()
    }

}
```

上面这段代码是后台任务的写法，每一个后台任务都必须继承自Worker类，并调用它唯一的构造函数，然后重写父类中的doWork()方法。doWork()方法要求返回一个result对象，用于表示任务的运行结果，成功就返回Result.success()，失败就返回Result.failure()，另外还有一个Result.retry()方法也代表着失败。

接下来就是配置该后台任务的运行条件和约束信息

```kotlin
val request = OneTimeWorkRequest.Builder(SimpleWorker::class.java).build()
```

OneTimeWorkRequest.Builder是WorkRequest.Builder的子类，用于构建单次运行的后台任务请求。WorkRequest.Builder的另一个子类PeriodicWorkRequest.Builder可用于构建周期性运行的后台任务请求，其传入的运行周期间隔不得短于15分钟。最后将构建出的后台任务请求传入WorkManager的enqueue()方法中，系统就会在合适的时间去运行了。

```kotlin
binding.doWorkBtn.setOnClickListener {
    val request = OneTimeWorkRequest.Builder(SimpleWorker::class.java).build()
    WorkManager.getInstance(this).enqueue(request)
}
```

#### （二）使用WorkManager处理复杂的任务

（1）延迟运行任务：借助setInitialDelay()方法

```kotlin
val request = OneTimeWorkRequest.Builder(SimpleWorker::class.java)
	.setInitialDelay(5, TimeUnit.MINUTES)	//这个后台任务在5分钟后运行
	.build
```

（2）给后台任务添加标签

添加标签可以让我们可以通过标签来取消后台任务请求

```kotlin
val request = OneTimeWorkRequest.Builder(SimpleWorker::class.java)
	...
	.addTag("simple")
	.build
...
WorkManager.getInstance(this).cancelAllWorkByTag("simple")	//可以取消所有同名标签的后台任务请求
```

没有标签也可以通过id来取消后台任务请求

```kotlin
WorkManager.getInstance(this).cancelWorkById(request.id)	//只能取消单个后台任务请求
```

也可以通过下面的方式取消全部后台任务请求

```kotlin
WorkManager.getInstance(this).cancelAllWork()
```

（3）重新执行任务

doWork()方法可以返回result.retry()，那么可以结合setBackOffCriteria()方法来重新执行任务

```kotlin
val request = OneTimeWorkRequest.Builder(SimpleWorker::class.java)
	...
	.setBackOffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)
	.build()
```

setBackOffCriteria()方法接收三个参数，第一个参数用于指定如果任务再次执行失败，下次重试的时间应该以什么样的形式延迟：LINEAR代表下次重试时间以线性的方式延迟，EXPONENTIAL代表下次重试时间以指数的方式延迟。第二和第三个参数用于指定在多久之后重新执行任务，时间最短不能小于10秒钟。

（4）对运行结果进行监听

```kotlin
WorkManager.getInstance(this)
	.getWorkInfoByIdLiveData(request.id)	//传入后台任务请求的id，会返回一个LiveData对象，然后就可以调用LiveData对象的observe()方法来观察数据变化了
	.observe(this) { workInfo ->
    	if (workInfo.state == WorkInfo.State.SUCCEEDED) {
            Log.d("MainActivity". "do work succeeded")
        } else if (WorkInfo.state == WorkInfo.State.FAILED) {
            Log.d("MainActivity", "do Work failed")
        }
     }
```

另外也可以使用getWorkInfoByTagLiveData()方法，通过监听同一标签名下所有后台任务的运行结果。

（4）链式任务

假设定义了三个独立的后台任务：同步数据、压缩数据和上传数据

```kotlin
val sync = ...
val compress = ...
val upload = ...
WorkManager.getInstance(this)
	.beginWith(sync)
	.then(compress)
	.then(upload)
	.enqueue()
```

beginWith()方法用于开启一个链式任务，后面接上的后台任务只需要使用then()方法链接即可。WorkManager要求必须在**前一个后台任务运行成功之后下一个后台任务才会运行**。
