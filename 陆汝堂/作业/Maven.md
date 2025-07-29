1.Maven中的仓库用来存储什么的?
Maven的仓库是用来存储和管理jar包的

2.Maven中有哪几类仓库?查找依赖(jar)的顺序是什么样的?
本地仓库(1)
远程仓库(2)
中央仓库(3)



什么是坐标?
Maven 中的坐标是资源(jar)的唯一标识，通过该坐标可以唯一定位资源位置

使用坐标来定义项目或引入项目中需要的依赖。

1.依赖配置的方式 ?

```
<dependencies>
<dependency>...</dependency>
</dependencies>
```

2.如何排除依赖?

```
<exclusions>...</exclusions>
```

3.注意事项
一旦依赖配置变更了，记得重新知载

引入的依赖本地仓库不存在，记得联网

maven生命周期

- clean:清理
- compile:编译
- test:测试
- package:打包install:安装



![image-20250715211723182](C:\Users\17811\AppData\Roaming\Typora\typora-user-images\image-20250715211723182.png)

![image-20250715212609853](C:\Users\17811\AppData\Roaming\Typora\typora-user-images\image-20250715212609853.png)

![image-20250715212643635](C:\Users\17811\AppData\Roaming\Typora\typora-user-images\image-20250715212643635.png)

![image-20250715212843956](C:\Users\17811\AppData\Roaming\Typora\typora-user-images\image-20250715212843956.png)

继承

继承描述的是两个过程间的关系，与java中的继承类似，子过程可以继承父工程的配置信息，常见于依赖关系的继承

作用

简化配置

减少 版本依赖



聚合与继承的区别
作用
聚合用于快速构建项目
继承用于快速配置
相同点:
聚合与继承的pom.xml文件打包方式均为pom，可以将两种关系制作到同一个pom文件中

聚合与继承均属于设计型模块，并无实际的模块内容
不同点:
聚合是在当前模块中配置关系，聚合可以感知到参与聚合的模块有哪些

继承是在子模块中配置关系，父模块无法感知哪些子模块继承了自己





属性的定义

```xml
<!--定义自定义属性-->
<properties>
<spring.version>5.2.10.RELEASE</spring.version>
    <junit.version>4.12</junit.version>
</properties>
```





属性的使用

```xml
<dependency>
<groupId>org.springframework</groupId>
<artifactId>spring-context</artifactId>
<version>${spring.version}</version>
</dependency>
```



资源文件的开启过滤

```xml
<build>
<resources>
<resource>
<directory>${project.basedir}/src/main/resources</directory><filtering>true</filtering>
</resource)
</resources>
</build>
```





定义多环境

```xml
<!--定义多环境-->
<profiles>
<!--定义具体的环境:生产环境-->
<profile>
<!--定义环境对应的唯一名称--
><id>env_dep</id>
<!--定义环境中专用的属性值-->
<properties>
<jdbc.url>jdbc:mysql://127.0.0.1:3306/ssm_db</jdbc.url></properties>
<!--设置默认启动-->
<activation>
<activeByDefault>true</activeByDefault>
</activation>
</profile>
<!--定义具体的环境:开发环境-->
<profile>
<id>env_pro</id>
.....
</profile>
</profiles>
```





nexus

打开  

```bash
.\bin\nexus.exe //ES//SonatypeNexusRepository
```

关闭

```bash
.\bin\nexus.exe //ES//SonatypeNexusRepository
```

密码：admin

![image-20250715223442573](C:\Users\17811\AppData\Roaming\Typora\typora-user-images\image-20250715223442573.png)

```xml
<mirror>
      <id>maven-public</id>
      <mirrorOf>*</mirrorOf>
      <url>http://localhost:8081/repository/maven-public/</url>
    </mirror>
```

```xml
    <server>
      <id>tzdphx-snapshot</id>
      <username>admin</username>
      <password>admin</password>
    </server>

    <server>
      <id>tzdphxx-release</id>
      <username>admin</username>
      <password>admin</password>
    </server>
```

```xml
<distributionManagement>
<repository>
<id>heima-release</id>
<url>http://localhost:8081/repository/heima-release/</url>
    </repository>
<snapshotRepository>
<id>heima-snapshots</id>
<url>http://localhost:8081/repository/heima-snapshots/</url>
    </snapshotRepository>
</distributionManagement>
```

