# MVP架构

## 1.MVC

Model（数据提供者）-View（控件）-Controller（活动）

缺点在于：
1.activity里面的控件必须关心业务和逻辑，才知道自己怎么展示	

2.所有的逻辑都在activity中



## 2.MVP

![image-20250714163037812](C:\Users\yyz20\AppData\Roaming\Typora\typora-user-images\image-20250714163037812.png)

Model-View-Presenter

优势：

1.使用 **MVP** 之后，**Activity** 就能瘦身许多了，基本上只有 **FindView、SetListener** 以及 **Init** 的代码。其他的就是对 **Presenter** 的调用，还有对 **View****接口** 的实现。这种情形下阅读代码就容易多了。

2.而且你只要看 **Presenter** 的接口，就能明白这个模块都有哪些业务，很快就能定位到具体代码。**Activity** 变得容易看懂，容易维护，以后要调整业务、删减功能也就变得简单许多。

3.方便进行单元测试

4.避免内存泄露：APP发生 **OOM** 的最大原因就是出现内存泄露造成APP的内存不够用，而造成内存泄露的两大原因之一就是 **Activity泄露（Activity Leak）**（另一个原因是 **Bitmap泄露（Bitmap Leak）**），java虚拟机对于被引用的对象由于还可能被调用所以不会回收，而MVP模式可以分离异步任务对引用
OOM：out of memory



步骤：
Model->提供数据的
View->UI逻辑（拿到Presenter层的结果进行UI更新）
Presenter->业务逻辑（对数据进行判断得到结果，把结果给到View层，让他进行UI更新）

Activity调用Present执行业务逻辑，Presenter层调用View层执行UI逻辑（所以Presenter需要以View接口作为参数设置主构造函数），具体的UI逻辑又由实现了View接口的 Activity去重写



## 3.MVVM

ViewModel 负责调用 Model（可以称之为数据源），拿到结果后，更新自身。而 View 与 ViewModel 双向绑定，所以 View 就会自动更新。 这就是 MVVM 大致的思想
![image-20250715145042020](C:\Users\yyz20\AppData\Roaming\Typora\typora-user-images\image-20250715145042020.png)

View 与 ViewModel 双向绑定是通过 `Data Binding` 库和 `ViewModel` 组件实现的