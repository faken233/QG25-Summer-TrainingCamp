# 使用步骤

1. 建表，添加数据

2. 创建模块，导入坐标

3. 编写Mybatis核心文件

   替换连接信息，解决硬编码问题

4. 编写SQL映射文件

   统一管理sql语句，解决硬编码问题

5. 编码

   1. 定义POJO类
   2. 加载核心配置文件，获取SqlSessionFactory 对象
   3. 获取SqlSession 对象，执行SQL语句
   4. 释放资源

## 使用 Mapper 代理

1. 定义与SQL映射文件同名的Mapper接口，并且将Mapper接口和SQL映射文件放置在同一目录下
2. 设置SQL映射文件的namespace属性为Mapper接口全限定名

3. 在 Mapper 接口中定义方法，方法名就是SQL映射文件中sql语句的id，并保持参数类型和返回值类型一致
4. 编码
   1. 通过 SqlSession 的 getMapper方法获取 Mapper接囗的代理对象
   2. 调用对应方法完成sql的执行
5. 细节:如果Mapper接口名称和SQL映射文件名称相同，并在同一目录下，则可以使用包扫描的方式简SQL映射文件的加载

### 实体类属性名 和 数据库表列名 不一致，不能自动封装数据

1. 起别名:在SQL语句中，对不一样的列名起别名，别名和实体类属性名一样*可以定义 <sql>片段，提升复用性
2. resultMap:定义<resultMap>完成不一致的属性名和列名的映射

条件查询：

1. 散装查询：用@param（"SQL参数占位符名称"） 来注入多个参数

   `List<Brand> selectByCondition(@Param("status") int status, @Param("companyName") String companyName, @Param("brandName") String brandName);`

2. 对象查询 ：SQL的参数名要和 实体类的属性名对应

3. map集合查询 ：SQL的参数名要和 map集合 的键名对应

动态：

if:用于判断参数是否有值，使用test属性进行条件判断

*存在的问题:第一个条件不需要逻辑运算符

1. 解决方案:
   使用恒等式让所有条件格式都一样

2. <where>标签替换 where 关键字

   `xml`

   ```
   <select id="selectByCondition" resultMap="brandResultMap">
       select *
       from tb_brand
       <where>
           <if test="status!=null">and status = #{status}</if>
   
           <if test="companyName != null and companyName !=''">and company_name like #{companyName}</if>
           <if test="brandName != null and brandName !=''">and brand_name like #{brandName}</if>
       </where>
   
   </select>
   ```

单条件的动态查询choose（when， otherwise）

`xml`

```
<select id="selectByConditionSignal" resultMap="brandResultMap">
    select *
    from tb_brand
    where
    <choose>
        <when test="status!=null">status = #{status}</when>
        <when test="companyName != null and companyName !=''">company_name like #{companyName}</when>
        <when test="brandName != null and brandName !=''">brand_name like #{brandName}</when>

        <otherwise>
            /*防止什么都不选*/1=1
        </otherwise>

    </choose>

</select>
```

添加：主键返回功能<insert id="add" useGeneratedKeys="true" keyProperty="id">

`xml`

```
<insert id="add" useGeneratedKeys="true" keyProperty="id">

    insert into tb_brand (brand_name, company_name, ordered, description, status)
    values (#{brandName}, #{companyName}, #{ordered}, #{description}, #{status});
</insert>
```

修改： 

`xml`

```
<insert id="update">
    update tb_brand
    <set>
        <if test="brandName != null and brandName != ''">brand_name = #{brandName},</if>
        <if test="companyName != null and companyName != ''">company_name = #{companyName},</if>
        <if test="ordered != null">ordered = #{ordered},</if>
        <if test="description != null and description != ''">description = #{description},</if>
        <if test="status != null">status = #{status},</if>

    </set>
    where id = #{id};
</insert>
```

删除：`xml`

```
<delete id="deleteById">
    delete from tb_brand
    where id
    in (
        <foreach collection="array" item="id" separator=",">
            #{id}
        </foreach>
              );
</delete>
```

要是是简单的SQL语句，可以用注解

- 查询：@Select
- 添加：@Insert
- 修改：@Update
- 删除：@Delete