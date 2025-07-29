spring

![image-20250714085714918](C:\Users\17811\AppData\Roaming\Typora\typora-user-images\image-20250714085714918.png)

目标:充分解耦

- 使用IoC容器管理bean(IoC)
- 在IoC容器内将有依赖关系的bean进行关系绑定(DI)最终效果
- 使用对象时不仅可以直接从IoC容器中获取，并且获取到的bean已经绑定了所有的依赖关系





- IoC(Inversion of Control)控制反转

  使用对象时，由主动new产生对象转换为由外部提供对象，此过程中对象创建控制权由程序转移到外部，此思想称为控制反转

- Spring技术对IoC思想进行了实现

  Spring提供了一个容器，称为IoC容器，用来充当IoC思想中的“外部

  IoC容器负责对象的创建、初始化等一系列工作，被创建或被管理的对象在IoC容器中统称为Bean

- DI(Dependency Injection)依赖注入

  在容器中建立bean与bean之间的依赖关系的整个过程，称为依赖注入



![image-20250714092414563](C:\Users\17811\AppData\Roaming\Typora\typora-user-images\image-20250714092414563.png)

![image-20250714092930653](C:\Users\17811\AppData\Roaming\Typora\typora-user-images\image-20250714092930653.png)

![image-20250714092950951](C:\Users\17811\AppData\Roaming\Typora\typora-user-images\image-20250714092950951.png)

bean的生命周期

- 初始化容器
  1. 创建对象(内存分配）
  2. 执行构造方法
  3. 执行属性注入(set操作)
  4. 执行bean初始化方法
- 使用bean
  1. 执行业务操作
- 关闭/销毁容器
  1. 执行bean销毁方法





1.定义bean
@component
@Controller
@Service
@Repository

< context:component-scan/ >
2.纯注解开发
@Configuration
@ComponentScan
AnnotationConfigApplicationContext

生命周期

```java
@Repository
@scope("singleton")
public class BookDaoImpl implements BookDao {
    public BookDaoImpl(){
		System.out.println("book dao constructor ...");
    }
	@PostConstruct
	public void init(){
        System.out.println("book init ...");
    }
	@PreDestroy
	public void destroy(){
        System.out.println("book destory ...");
    }
}
```

spring 整合 mybatis

```java
public class MybatisConfig{
	@Bean
	public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource){
		SalSessionFactoryBean ssfb =new SqlSessionFactoryBean();
		ssfb.setTypeAliasesPackage("com.itheima.domain");
		ssfb.setDataSource(dataSource);
		return ssfb;
	}
	@Bean
	public MapperScannerConfigurer mapperScannerConfigurer(){
		MapperScannerConfigurer msc =new MapperScannerConfigurer();
		msc.setBasePackage("com.itheima.dao");
		return msc;
	}
}
```



spring整合junit

```java
使用Spring整合Junit专用的类加载器
@RunWith(springUnit4classRunner.class)
@Contextconfiguration(classes =SpringConfig.class)
public class BookserviceTest {
	@Autowired
	private BookService bookService.
	@Test
	public void testsave(){
	bookService.save();
	}
}
```





### AOP核心概念



- 连接点(JoinPoint):程序执行过程中的任意位置，粒度为执行方法、抛出异常、设置变量等

  ​	在SpringAoP中，理解为方法的执行

- 切入点(Pointcut):匹配连接点的式子

  ​	在SpringAoP中，一个切入点可以只描述一个具体方法，也可以匹配多个方法

  ​		一个具体方法:com.itheima.dao包下的BookDao接口中的无形参无返回值的save方法

  ​		匹配多个方法:所有的save方法，所有的get开头的方法，所有以Dao结尾的接口中的任意方法，所有带有一个参数的方法

- 通知(Advice):在切入点处执行的操作，也就是共性功能

  ​	在SpringAoP中，功能最终以方法的形式呈现

- 通知类:定义通知的类

- 切面(Aspect):描述通知与切入点的对应关系



```java
@component
@Aspect
public class MyAdvice {
	
	@Pointcut("execution(void com.itheima.dao.BookDao.save())")
	private void ptx(){}
	
	@Pointcut("execution(void com.itheima.dao.BookDao.update())")
	private void pt(){}
	
	@Before("pt()")
	public void method(){
	
	System.out.println(System.currentTimeMillis());
}
```

环绕通知

```java
@Around("pt()")
public Object around(ProceedingJoinPoint pjp) throws Throwable {
	System.out.println("around before advice ...");
	Object ret = pjp.proceed();
	System.out.println("around after advice ...”);
	return ret;
}
```

@Around注意事项

1. 环绕通知必须依赖形参ProceedingjoinPoint才能实现对原始方法的调用，进而实现原始方法调用前后同时添加通知
2. 通知中如果未使用ProceedingJoinPoint对原始方法进行调用将跳过原始方法的执行
3. 对原始方法的调用可以不接收返回值，通知方法设置成void即可，如果接收返回值，必须设定为Object类型
4. 原始方法的返回值如果是void类型，通知方法的返回值类型可以设置成void，也可以设置成Object
5. 由于无法预知原始方法运行后是否会抛出异常，因此环绕通知方法必须抛出Throwable对象

```java
@Around("ProjectAdvice.servicePt()")
public void runspeed(ProceedingJoinPoint pjp)throws Throwable {/获取执行签名信息
	Signature signature = pjp.getSignature();
	/通过签名获取执行类型(接口名)
	String className =signature.getDeclaringTypeName();
	/通过签名获取执行操作名称(方法名)
	String methodName =signature.getName();
	long start =System.currentTimeMillis();
	for(int i=0;i<10000;i++){
		pjp.proceed();
	}
	long end=System.currentTimeMillis();
	System.out.println("万次执行:"+className+"."+methodName+"--->"+(end-	start)+"ms");
}
```

事务管理

```java
@Bean
public PlatformTransactionManager transactionManager(DataSource dataSource){
	DataSourceTransactionManager ptm = new DataSourceTransactionManager()；
	ptm.setDataSource(datasource);
	return ptm;
}
```

