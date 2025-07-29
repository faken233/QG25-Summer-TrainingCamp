```java
@Configurationpublic class Mpcongfig {
	@Bean
	public MybatisPlusInterceptor pageInterceptor(){
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor()；
		interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
		return interceptor;
	}
}
```

MP分页查询的功能配置







日志开启

```properties
mybatis-plus:
	configuration:
		log-impl:org.apache.ibatis.logging.stdout.stdoutImpl
```





条件查询

```java
QueryWrapper<User>qw=new QueryWrapper<User>();
//查询年龄大于等于18岁，小于65岁的用户
qw.lt("age",65);
qw.gt("age",18);
List<User>userList =userDao.selectList(qw);
System.out.println(userList);
```

```java
QueryWrapper<User>qw=new QueryWrapper<User>();
//查询年龄大于等于18岁，小于65岁的用户
qw.lt("age",65).gt("age",18);
List<User>userList =userDao.selectList(qw);
System.out.println(userList);
```

```java
QueryWrapper<User>qw=new QueryWrapper<User>();
//查询年龄大于等于18岁，小于65岁的用户
qw.lambda().lt(User::getAge,65).gt(User::getAge,18);
List<User>userList =userDao.selectList(qw);
System.out.println(userList);
```

```java
LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<User>();
//查询年龄大于等于18岁，小于65岁的用户
lqw.lt(User::getAge,65).gt(User::getAge,18);
List<User>userList =userDao.selectList(lqw);
System.out.println(userList);
```





条件参数控制

```java
LambadaQQueryWrapper<User> lqw = new LambadaQQueryWrapper<User>()；
lqw.ge(null != userQuery.getAge(),User::getAge,userQuery.getAge());
lqw.lt(null != userQuery.getAge(),User::getAge,userQuery.getAge());
List<User> userList userDao.selectList(lqw)；
System.out.println(userlist)
```





查询投影

```java
LambdaQueryWrapper<User>lqw= new LambdaQueryWrapper<User();
lqw.select(User::getId,User::getName,User::getAge);
List<User>userList =userDao.selectList(lqw);
System.out.println(userList);
```

```java
QueryWrapper<User>qm=new QueryWrapper<User>();
qm.select("count(*) as nums,gender");
qm.groupBy("gender");
List<Map<String,Object>>maps =userDao.selectMaps(qm);
System.out.println(maps);
```





用户登录

```java
LambdaQueryWrapper <User> lqw = new LamdaQueryWrapper <User> ();
lqw.eq(User::getName, userQuery.getName()).eq(User::getPassword,userQuery.getPassword());
User loginUser = userDao.selectOne (lqw);
System.out.println(loginUser);
```



设置区间

```java
LambdaQueryWrapper<User> lqw= new LambdaQueryWrapper<User>();
//方案一:设定上限下限
1qw.le(User::getAge,userQuery.getAge()).ge(User::getAge,userQuery.getAge2());
//方案二:设定范围
lqw.between(User::getAge,userQuery.getAge(),userQuery.getAge2());
List<User>userList =userDao.selectList(lqw);
System.out.println(userList);
```



like匹配

```java
LambdaQueryWrapper<User>lqw= new LambdaQueryWrapper<User>();
lqw.likeLeft(User::getTel,userQuery.getTel());
List<User>userList =userDao.selectList(lqw);
System.out.println(userList);
```



分组查询的聚合

```java
QueryWrapper<User>qw=new QueryWrapper<User>();
qw.select("gender","count(*)as nums");
qw.groupBy("gender");
List<Map<String,object>>maps = userDao.selectMaps(qw);
System.out.println(maps);
```



注解

@TableField(value="psw",select=false)    映射到字段psw且不给查询

@TableField(exist= "false")    表示 表里面 不存在 无需查询

@TableName("tbl_book")   映射到表名

@TableLogic  逻辑删除

 @Version 乐观锁