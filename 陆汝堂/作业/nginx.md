# Nginx

nginx 是一个高性能的HTTP和反向代理web服务器

**正向代理：如果把局域网外的 Internet 想象成一个巨大的资源库，则局域网中的客户端要访 问 Internet，则需要通过代理服务器来访问，这种代理服务就称为正向代理。**

![正向代理](C:\Users\17811\Pictures\Screenshots\屏幕截图 2025-07-09 162808.png)

反向代理：我们**需要将请求发送到反向代理服务器，由反向代理服务器去选择目标服务器获取数据后，在返 回给客户端**，此时反向代理服务器和目标服务器对外就是一个服务器，**暴露的是代理服务器 地址，隐藏了真实服务器 IP 地址。**

![反向代理](C:\Users\17811\Pictures\Screenshots\屏幕截图 2025-07-09 162831.png)

负载均衡

增加服务器的数量，然后将请求分发到各个服务器上，将原先请求集中到单个服务器上的 情况改为将请求分发到多个服务器上，将负载分发到不同的服务器，也就是我们所说的负 载均衡

客户端发送多个请求到服务器，服务器处理请求，有一些可能要与数据库进行交互，服 务器处理完毕后，再将结果返回给客户端。

动静分离

为了加快网站的解析速度，可以把动态页面和静态页面由不同的服务器来解析，加快解析速 度。降低原来单个服务器的压力。



常用命令

./nginx

./nginx -s stop

./nginx -s quit

./nginx -s reload 

ps aux | grep nginx





配置文件conf

分为三个部分：全局块、events块、http块

全局块

作用：从配置文件开始到 events 块之间的内容，主要会设置一些影响[nginx 服务器](https://so.csdn.net/so/search?q=nginx 服务器&spm=1001.2101.3001.7020)整体运行的配置指令，主要包括配 置运行 Nginx
服务器的用户（组）、允许生成的 worker process 数，进程 PID 存放路径、日志存放路径和类型以 及配置文件的引入等。

```powershell
worker_processes  1;
```

这是 Nginx 服务器并发处理服务的关键配置，`worker_processes 值越大，可以支持的并发处理量也越多`，但是 会受到硬件、软件等设备的制约。



events块

作用：events 块涉及的指令主要影响 Nginx 服务器与用户的网络连接，常用的设置包括是否开启对多 work process 下的网络连接进行序列化，是否 允许同时接收多个网络连接，选取哪种事件驱动模型来处理连接请求，每个 word process 可以同时支持的最大连接数等。

```powershell
worker_connections  1024;
```



http块

作用：这算是 Nginx 服务器配置中最频繁的部分，代理、缓存和日志定义等绝大多数功能和第三方模块的配置都在这里。

需要注意的是：http 块也可以包括 http全局块、server 块。

​	http全局块

​	http全局块配置的指令包括文件引入、MIME-TYPE 定义、日志自定义、连接超时时间、单链接请求数上限等。

​	server块

​	这块和虚拟主机有密切关系，虚拟主机从用户角度看，和一台独立的硬件主机是完全一样的，该技术的产生是为了 节省互联网服务	器硬件成本。每个 http 块可以包括多个 server 块，而每个 server 块就相当于一个虚拟主机。 而每个 server 块也分为全局
​	server 块，以及可以同时包含多个 locaton 块。

​		全局server块

​		最常见的配置是本虚拟机主机的监听配置和本虚拟主机的名称或IP配置。

​		location块

​		这块的主要作用是基于 Nginx 服务器接收到的请求字符串（例如 server_name/uri-string），对虚拟主机名称 （也可以是IP 别		名）之外的字符串（例如 前面的 /uri-string）进行匹配，对特定的请求进行处理。地址定向、数据缓 存和应答控制等功能，还		有许多第三方模块的配置也在这里进行。

```powershell
#user  nobody;
worker_processes  1;

events {
    worker_connections  1024;
}

http {
	#日志的输出
    log_format backend '$remote_addr - $host [$time_local] '
                      '"$request" $status $body_bytes_sent '
                      '"$http_referer" "$http_user_agent" '
                      'upstream_addr:$upstream_addr';

    access_log /var/log/nginx/access.log backend;
upstream backend_servers{
	#两个负载
    server 192.168.148.100:8080;
    server 192.168.148.100:8081;
}
    access_log /var/log/nginx/access.log;
    error_log  /var/log/nginx/error.log;
    include       mime.types;
    default_type  application/octet-stream;
    sendfile        on;
    keepalive_timeout  65;

    server {
        listen       80;
        server_name  192.168.148.100;
        location / {
            proxy_pass http://backend_servers;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            root   /usr/local/nginx/html;
            index  index.html index.htm login.html;  #自己的起始页面
        }   
            
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        } 
    }         
}     
```

