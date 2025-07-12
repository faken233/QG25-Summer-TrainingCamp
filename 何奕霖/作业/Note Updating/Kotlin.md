# Kotlin (Android Studio)

##  变量与函数

### 1.变量

在Kotlin中只有两种变量，分别是**val**和**var**，其中val对应的是**不可重新赋值的变量**（相当于java中的final变量），var对应的是**可以重新赋值的变量**（对应java中的非final变量）

Kotlin中有一种自动类型推导机制，但并不总是能够正常工作的，这时就要显示地声明变量类型

```
val a: Int = 10    
```

这里Int是**大写开头**的，它的含义与java中的int不同，在java中int表示的是**整型**，而在Kotlin中Int表示的是一个**类**，有自己的方法和继承结构。                                                                    

### 2.函数

在函数中用fun关键字开头，fun表示的是function

①if条件语句





接口...



## 数据类

一个类前声明了data关键字时，这个类就成为了一个数据类

```
data class Cellphone(val brand: String, val price: Double)
```

在数据类中会自动生成下列方法：equals()  hashCode()  toString()

## 单例类

单例模式用于避免创建重复的对象

```
object Singleton {  将普通类的class关键字改为object关键字即可
    fun singletonTest() {
        println("singletonTest is called")
    }
}
```

## Lamda编程

### 1.集合的创建与遍历

Kotlin中有一个内置的lishOf()函数用于简化初始化集合

```
val list = listOf("Apple", "Banana", "Orange", "Pear", "Grape")
```

Kotlin中有for-in循环用于遍历集合

```
val list = listOf("Apple", "Banana", "Orange", "Pear", "Grape")
for (i in list) {
    println(i)
}
```

但是需要注意的是这里的listOf()创建的集合是一个不可变的集合，即不能作出添加、修改或删除；如果要创建可变的集合，可以使用mutablelistOf()函数

```
val list = mutableListOf("Apple", "Banana", "Orange", "Pear", "Grape")
list.add("Watermelon")
for (i in list) {
    println(i)
}
```

Set集合与List集合的用法几乎一模一样，但是Set集合中不可以存放相同的元素；

Map集合是一种键值对形式的数据结构，与前两个集合有较大不同。传统的Map首先要创建一个HashMap实例，接着将一个个键值对数据添加到Map中

```
val map = HashMap<String, Int>()
map["Apple"] = 1
map["Banana"] = 2
map["Orange"] = 3
...
```

当然可以使用mapOf()函数和mutablemapOf()函数来简化创建过程

```
val map = mapOf("apple" to 1, "banana" to 2, "orange" to 3, "grape" to 4)
for((fruit,number) in map) {
    println("fruit is " + fruit + ", number is " + number)
}
```

### 2.集合的函数式API

map，filter，any，all

### 3.Java函数式API的使用

## 空指针检查

1.可空类型系统

传入参数时在数据类型后加上？，这样一来传入的参数就可以为空了。

2.判空辅助工具

?.操作符：这个操作符的做就就是当对象不为空时正常调用相应的方法，当对象为空时则什么都不做。例如

```
fun doStudy(study:Study?) {
    study?.readBooks()
    study?.doHomework()
}
```

这样编写程序就可以省去if判断语句。

?:操作符：这个操作符左右各接受一个表达式，如果左边的表达式的结果不为空就返回左边表达式的结果，否则就返回右边表达式的结果。例如

```
val c = a ?: b
```

除此之外还有!!等判空符

## 标准函数和静态方法

### 1.标准函数with、run和apply

Kotlin地标准函数指的是Standard.kt文件中定义的函数，任何Kotlin代码都可以自由地调用所有的标准函数

#### （一）with函数

with函数接收两个参数：第一个参数可以是一个任意类型的对象，第二个参数是一个Lambda表达式.With函数会在Lambda表达式中提供第一个参数对象的上下文，并使用Lambda表达式中的最后一行代码作为返回值返回。

```
val result = with(obj) {
	//obj的上下文
	"value"		//with函数的返回值
}
```

下面是一个具体的使用with函数的例子

```
fun main() {
    val list = listOf("Apple", "Banana", "Orange", "Grape")
    val result = with(StringBuilder()) {	//with函数的第一个参数是StringBuilder对象
        append("Start eating fruits.\n")
        for( fruit in list) {
            append(fruit).append("\n")
        }
        append("Ate all fruits.\n")
        toString()
    }
    println(result)
}
```

#### （二）run函数

以刚才的例子为基础，run函数将调用with函数并传入StringBuilder对象改成了调用StringBuilder的run方法

```
val list = listOf("Apple", "Banana", "Orange", "Grape")
val result = StringBuilder().run {		//调用StringBuilder对象的run方法
    append("Start eating fruits.\n")
    for( fruit in list) {
        append(fruit).append("\n")
    }
    append("Ate all fruits.\n")
    toString()
}
println(result)
```

这两段代码的最终运行效果是一样的.

#### （三）apply函数

apply函数与run函数较为相似，不同的是apply函数无法指定返回值，会自动返回调用对象本身

```
val list = listOf("Apple", "Banana", "Orange", "Grape")
val result = StringBuilder().apply {		
    append("Start eating fruits.\n")
    for( fruit in list) {
        append(fruit).append("\n")
    }
    append("Ate all fruits.\n")
    toString()
}
println(result.toString)
```

这段代码中的result实际上是一个StringBuilder对象，所以在1最后打印的时候要再调用它的toString()方法才行。

### 2.定义静态方法

#### （一）注解

给单例类或companion object中的方法加上@JvmStatic注解，那么Kotlin编译器就会将这些方法编译成真正的静态方法

```
class Util {
    fun doAction1() {
        println("do action1")
    }
    
    companion object {
        @JvmStatic
        fun doAction2() {
            println("do action2")
        }
    }
}
```

注意@JvmStatic注解只能加在单例类或companion object中的方法上。这之后doAction2已经是一个真正的静态方法了，可以使用Util.doAction2()的写法来调用了。

#### （二）顶层方法

顶层方法指的是那些没有定义在任何类中的方法。Kotlin编译器会将所有的顶层方法全部编译成静态方法

## 延迟初始化和密封类

### 1.对变量延迟初始化

当我们定义全局变量时，为了通过编译就要设置判空处理；也就是说如果有大量的全局变量，那么就需要编写大量额外的判空处理，否则无法通过编译。但如果我们对全局变量进行延迟初始化，那就可以规避这个问题。

延迟初始化使用的是lateinit关键字，它可以告诉Kotlin编译器在晚些时候进行初始化，这样就不用在一开始的时候将它赋值为null了。

```kotlin
class MainActivity : AppCompatActivity(), View.OnClickListener {
	
	private lateinit var adapter: MsgAdapter
	
	override fun onCreate(savedInstanceState: Bundle?) {
        ...
        adapter = MsgAdapter(msgList)
        ...
    }
    
    override fun onClick(v: View?) {
        ...
        adapter.notifyItemInserted(msgList.size - 1)
        ...
    }
    
}
```

这段代码就使用了延迟初始化的方法，否则要在定义时加上?= null， 还要在onClick方法中对adapter进行判空操作。

但是需要注意的是，一个全局变量使用了lateinit关键字时，一定要确保它在被调用之前已经完成了初始化，否则程序会崩溃。

这里可以使用代码判断一个全局变量是否已经完成了初始化，能在某些时候有效避免重复初始化。

```kotlin
class MainActivity : AppCompatActivity(), View.OnClickListener {
    
    private lateinit var adapter : MsgAdapter
    
    pverride fun onCreate(savedInstanceState:Bundle?) {
        ...
        if(!::adapter.isInitialized) {
            adapter = MsgAdapter(msgList)
        }
        ...
    }
    
}
```

### 2.使用密封类优化代码

密封类（sealed class）用于在有限个子类时，Kotlin编译器会自动检查该密封类中有哪些子类，并强制要求将每一个子类对应的条件全部处理，这样可以保证即使没有写else条件也不会出现漏写条件分支的情况。

## 扩展函数和运算符重载

### 1.扩展函数

一种更加面向对象的思维。

定义扩展函数只需要在函数名的前面加上一个ClassName.的语法结构，就表示将该函数添加到指定类当中了。如

```kotlin
fun ClassName.methodName(param1: Int, param2: Int): Int {
	return 0
}
```

假设现在有一个用于统计字符串中英文字母个数的单例类

```kotlin
object StringUtil {

    fun lettersCount(string: String): Int {
        var count = 0
        for ( char in string) {
            if (char.isLetter()) {
                count++
            }
        }
        return count
    }
}
```

那么对用的扩展函数可以这样写

```kotlin
fun String.lettersCount():Int {
    var count = 0
    for (char in this) {
        if (char.isLetter()) {
            count++
        }
    }
    return count
}
```

这里String.就将lettersCount()方法定义为了String类的扩张函数，那么函数中就自动拥有了String实例的上下文，因此lettersCount()函数就不用再单独接收字符串参数了，而是直接遍历this即可，因为此时this就代表着字符串本身。

之后再调用方法时这样写即可

```kotlin
val count = "asdfghjkl12345678".lettersCount()
```

### 2.运算符重载

运算符重载是Kotlin的一个语法糖 ，让任意两个对象相加或是进行更多其他的运算操作。

运算符重载使用operator关键字，在指定函数的前面加上operator关键字就可以实现运算符重载的功能了。

以加号运算符为例

```kotlin
class Obi {
    operator fun plus(obi: Obi): Obj {
        //处理相加的逻辑
    }
}
```

上述代码接受的参数和函数返回值可以根据逻辑自行设定。表示一个Obj对象可以与另一个Obj对象相加，最终返回一个新的Obj对象

```kotlin
val obj1 = Obj()
val obj2 = Obj()
val obj3 = obj1 + obj2
```

这在编译的时候会被转成obj1.plus(obj2)的调用方式。以上就是运算符重载的基本语法。

如果我们定义了一个类结构，接下来就可以使用运算符重载来实现两个对象的四则运算

```kotlin
class Money(val value: Int) {

    operator fun plus(money: Money): Money {
        val sum = value + money.value	//将当前Money对象的value和参数传入的Money对象的value相加
        return Money(sum)
    }
    
}
```

这里定义了一个Money类，并用运算符重载来写一个让两个Money对象相加的逻辑

```kotlin
fun main() {
    val money1 = Money(5)			//传入value为5
    val money2 = Money(10)			//传入value为10
    val money3 = money1 + money2	//将两个对象相加
    println(money3.value)			//打印和的value值
}
```

当然Money对象也可以直接和一个数字相加，这里要用到的方法是多重重载。

```kotlin
class Money(val value: Int) {

    operator fun plus(money: Money): Money {
        val sum = value + money.value
        return Money(sum)
    }

    operator fun plus(newValue: Int) :Money {
        val sum = value + newValue
        return Money(sum)
    }
}
```

```kotlin
fun main() {
    val money1 = Money(5)
    val money2 = Money(10)
    val money3 = money1 + money2
    val money4 = money3 + 20
    println(money4.value)
}
```

| 语法糖表达式 | 实际调用函数   |
| ------------ | -------------- |
| a + b        | a.plus(b)      |
| a - b        | a.minus(b)     |
| a * b        | a.times(b)     |
| a / b        | a.div(b)       |
| a % b        | a.rem(b)       |
| a++          | a.inc()        |
| a--          | a.dec()        |
| +a           | a.unaryPlus()  |
| -a           | a.unaryMinus() |
| !a           | a.not()        |
| a == b       | a.equals(b)    |
| a > b        | a.compareTo(b) |
| a < b        | a.compareTo(b) |
| a>= b        | a.compareTo(b) |
| a <= b       | a.compareTo(b) |
| a..b         | a.rangeTo(b)   |
| a[b]         | a.get(b)       |
| a[b] = c     | a.set(b, c)    |
| a in b       | b.contains(a)  |

