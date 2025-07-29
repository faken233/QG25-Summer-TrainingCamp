# Gson

Gson 是 Google 提供的一个 Java 库，用于将 Java 对象转换为 JSON 表示（序列化），以及将 JSON 字符串转换为 Java 对象（反序列化）。

1.

```java
//java对象转json gson.toJson(序列化)
Gson gson=new Gson();
Person person1=new Person("张三",18);
String json=gson.toJson(person1);
System.out.println(json);
//Json转java（反序列化）
Person parsedPerson=gson.fromJson(json, Person.class);
System.out.println(parsedPerson.getName());
//json转java
String json1="{\"name\":\"李四\",\"age\":\"18\"}";
Person person2=gson.fromJson(json1,Person.class);
System.out.println(person2.getName());
```

注意Person中要提供getter和setter方法

2.集合

```java
Gson gson=new Gson();
//asList可以将任意数量的元素直接包装成一个大小固定的List，省去了add，但是不能修改
List<Person>people= Arrays.asList(new Person("王五",28),new Person("赵六",32));
String jsonList=gson.toJson(people);
System.out.println(jsonList);
//打印[{"name":"王五","age":28},{"name":"赵六","age":32}]

*// 反序列化集合*
    //处理泛型集合
 Type listType=new TypeToken<List<Person>>(){}.getType();//用来告诉fromJson，List里面是Person类型的元素
        List<Person>parsePeople=gson.fromJson(jsonList,listType);
        System.out.println(parsePeople.get(1).getName());
```

3.日期

```java
//序列化
Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:aa").create();
Date now = new Date();
String dateJson = gson.toJson(now);
System.out.println(dateJson);

//反序列化
Date parseDate = gson.fromJson(dateJson, Date.class);
System.out.println(parseDate);
```