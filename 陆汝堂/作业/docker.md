1.什么是镜像?
将应用所需的运行环境、配置文件、系统函数库等与应用一起打包得到的就是镜像
2.什么是容器?
为每个镜像的应用进程创建的隔离运行环境就是容器
3.什么是镜像仓库?
存储和管理镜像的平台就是镜像仓库
DockerHub是目前最大的镜像仓库，其中包含各种常见的应用镜像



1.docker run命令中的常见参数:

-d :让容器后台运行
--name:给容器命名，唯一
-e:环境变量
-p :宿主机端口映射到容器内端口
2.镜像名称结构:
Repository:TAG
镜像名      版本号

版本不写就是默认最新



1.什么是数据卷?
数据卷是一个虚拟目录，它将宿主机目录映射到容器内目录，方便我们操作容器内文件，或者方便迁移容器产生的数据
2.如何挂载数据卷?
在创建容器时，利用-v数据卷名:容器内目录完成挂载容器创建时，如果发现挂载的数据卷不存在时，会自动创建

3.数据卷的常见命令有哪些?

| 命令                  | 说明                   |
| --------------------- | ---------------------- |
| docker volume create  | 创建数据卷<br/>        |
| docker volume ls      | 查看所有数据卷<br/>    |
| docker volume rm      | 删除指定数据卷         |
| docker volume inspect | 查看某个数据卷的详情   |
| docker volume prune   | 清除所有未使用的数据卷 |

命令: docker run--name 容器名 -p 宿主机端口:容器端口 -v 宿主机目录或文件:容器内目录或文件 镜像名
注意:
本地目录必须以 /或 ./开头，如果直接以名称开头，会被识别为数据卷而非本地目录

-v mysql:/var/lib/mysql 会被识别为一个数据卷，数据卷叫mysql

-v  ./mysql:/var/lib/mysql 会被识别为当前目录下的mysql目录





```
docker run -d --name nginx -p 80:80 -v html:/usr/share/nginx/html nginx:1.20.2
```



| 指令       | 说明                                         | 示例                            |
| ---------- | -------------------------------------------- | ------------------------------- |
| FROM       | 指定基础镜像                                 | FROM centos:7                   |
| ENV        | 设置环境变量，可在后面指令使用               | ENV key=value                   |
| COPY       | 拷贝本地文件到镜像的指定目录                 | COPY ./jdk17.tar.gz /tmp        |
| RUN        | 执行Linux的shell命令，一般是安装过程的命令   | RUN tar -zxvf /tmp/jdk17.tar.gz |
| EXPOSE     | 指定容器运行时监听的端口，是给镜像使用者看的 | EXPOSE 8080                     |
| ENTRYPOINT | 镜像中应用的启动命令，容器运行时调用         | ENTRYPOINT java -jar xx.jar     |



| 指令                      | 说明                     |
| ------------------------- | ------------------------ |
| docker network create     | 创建一个网络             |
| docker network ls         | 查看所有网络             |
| docker network rm         | 删除指定网络             |
| docker network prune      | 清除未使用的网络         |
| docker network connect    | 使指定容器连接加入某网络 |
| docker network disconnect | 使指定容器连接离开某网络 |
| docker network inspect    | 查看网络详细信息         |



dockercompose

Docker Compose通过一个单独的docker-compose.yml 模板文件(YAML 格式)来定义一组相关联的应用容器，我们实现多个相互关联的Docker容器的快速部署，
帮助

| 类型     | 参数或指令 | 说明                        |
| -------- | ---------- | --------------------------- |
| 0ptions  | -f         | 指定compose文件的路径和名称 |
|          | -p         | 指定project名称             |
| Commands | up         | 创建并启动所有service容器   |
|          | down       | 停止并移除所有容器、网络    |
|          | ps         | 列出所有启动的容器          |
|          | logs       | 查看指定容器的日志          |
|          | stop       | 停止容器                    |
|          | start      | 启动容器                    |
|          | restart    | 重启容器                    |
|          | top        | 查看运行的进程              |