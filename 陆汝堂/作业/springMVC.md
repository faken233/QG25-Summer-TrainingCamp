注册tomcat

```java
public class ServletContainersInitConfig extends AbstractDispatcherServletInitializer {
    protected WebApplicationContext createServletApplicationContext(){
        AnnotationConfigWebApplicationContext ctx= new AnnotationConfigWebApplicationcontext();
        ctx.register(SpringMvcConfig.class);
        return ctx;
    }
	protected string[]getServletMappings(){
        return new string[]{"/"};
    }
	protected WebApplicationContext createRootApplicationcontext(){
        return null;
    }
}
```

以下一样

```java
public class ServletContainersInitConfig extends AbstractAnnotationConfigDispatcherServletInitializer {


    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{SpringConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{SpringMvcConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
```

 

@RequestBody与@RequestParamx区别
区别
@RequestParam用于接收url地址传参，表单传参【application/x-www-form-urlencoded)

@RequestBody用于接收json数据【application/json)  
应用
后期开发中，发送json格式数据为主，@RequestBody应用较广

.如果发送非json格式数据，选用@RequestParam接收请求参数



![image-20250714223714191](C:\Users\17811\AppData\Roaming\Typora\typora-user-images\image-20250714223714191.png)