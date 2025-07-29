# **第五章  Fragment**

1.创建两个碎片布局

2.创建两个碎片类继承自Fragment（）（注意这里要用androdX库中的），重写onCreateView方法，把布局加载进来

```kotlin
class LeftFragment:Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.left_fragment,container,false)
    }
}
```

3.在MainActivity中加入两个碎片布局，注意碎片名要写完整包名路径：

```kotlin
<fragment
    android:id="@+id/leftFrag"
    android:name="com.example.fragmenttest.LeftFragment"
    android:layout_width="0dp"
    android:layout_height="match_parent"
    android:layout_weight="1"
```

动态加载碎片

把碎片添加到返回栈中，按下返回键不会直接退出程序，而是回到上一个碎片状态

```kotlin
private fun replaceFragment(fragment: Fragment) {
    val fragmentManager = supportFragmentManager
    val transaction = fragmentManager.beginTransaction()//创建事务
    transaction.replace(R.id.rightLayout, fragment)
    transaction.addToBackStack(null)//把事务传到返回栈中，参数传null
    transaction.commit()
}
```

## **Kotlin 课堂：扩展函数和运算符重载：**

### 扩展函数

扩展函数表示在不修改类中源码的情况下去添加函数。
  1.新建一个Kotlin文件，最好定义成顶层方法（不放在类中的方法），这样扩展函数就具有全局的访问域

```kotlin
fun String.lettersCount():Int{//向哪个类中添加扩展函数，就用类名. 函数名
    var count=0
    for(char in this){//自动联系String实例的上下文，直接传this
        if(char.isLetter())//判断是否是字母
        {
            count++
        }
    }
    return count
}
```

String类中还有：reversed（）反转字符串

​             capitalize（）对首字母大写

### 运算符重载

1.想让什么类的对象相加，就在哪个类中使用运算符重载

```kotlin
class Money(val value:Int) {
    //重载的关键字是operator
    operator fun plus (money:Money):Money{//plus是+的重载
        val sum=value+money.value
        return Money(sum)
    }
    
    operator  fun plue(newValue:Int):Money{// 对同一个运算符进行多重重载
        val sum=value+newValue
        return Money(sum)
    }
}
```

![image-20250711210707705](C:\Users\yyz20\AppData\Roaming\Typora\typora-user-images\image-20250711210707705.png)