# NIO基础

channel有一点类似于 stream，它就是读写数据的双向通道，可以从 channel将数据读入 buffer，也可以将buffer 的数据写入 channel，而之前的 stream 要么是输入，要么是输出，channel 比stream 更为底层

常见的 Channel 有

FileChannel

DatagramChannel

SocketChannel

ServerSocketChannel

buffer 则用来缓冲读写数据，常见的 buffer 有
ByteBuffer

- MappedByteBuffer
-  DirectByteBuffer
-  HeapByteBuffer

![image-20250717145017470](C:\Users\17811\AppData\Roaming\Typora\typora-user-images\image-20250717145017470.png)

ByteBuffer的使用

1. 向 buffer 写入数据，例如调用 channel.read(buffer)
2. 调用 fip() 切换至读模式
3. 从 buffer 读取数据，例如调用 buffer.get()
4. 调用 clear() 或 compact()切换至写模式
5. 重复 1~4 步骤

```java
package com.tzdphxx;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class TestByteBuffer {
    public static void main(String[] args) {
        try (FileChannel channel = new FileInputStream("E:/java/java_code/netty-demo/src/test/resources/data.txt").getChannel()){
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            while (channel.read(buffer) != -1) {

                // Prepare the buffer for reading
                buffer.flip();

                while (buffer.hasRemaining()) {
                    byte b = buffer.get();
                    System.out.println((char) b);
                }
                buffer.clear(); // Clear the buffer for the next read
            }

        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

```

```java
Bytebuffer buf = ByteBuffer.allocate(16); //内存分配
```

```java
package com.tzdphxx;

import java.nio.ByteBuffer;

public class TestByteBuffer2 {
    public static void main(String[] args) {

        String str1 = "Hello,World!\nI`m a Java developer.\nH";
        String str2 = "ow are you?";

        ByteBuffer source = ByteBuffer.allocate(64);
        source.put(str1.getBytes());
        split(source);
        source.put(str2.getBytes());
        split(source);

    }


    private static void split(ByteBuffer source){
        source.flip();
        for (int i = 0; i < source.limit(); i++) {
            if (source.get(i) == '\n') {
                int len = i + 1 - source.position();
                ByteBuffer bf = ByteBuffer.allocate(len);
                for (int j = 0; j < len; j++) {
                    bf.put(source.get());
                }
            }
        }
        source.compact();
    }

}

```

​            

文件夹和文件的遍历

```java
package com.tzdphxx;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

public class TestFileWalkFileTree {
    public static void main(String[] args) throws IOException {
        AtomicInteger dirCount = new AtomicInteger(0);
        AtomicInteger fileCount = new AtomicInteger(0);
        Files.walkFileTree(Paths.get("E:\\java\\java_code\\netty-demo"),new SimpleFileVisitor<Path>(){

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                System.out.println("dir==>"+dir.toString());
                dirCount.incrementAndGet();
                return super.preVisitDirectory(dir, attrs);
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println("file==>"+file.toString());
                fileCount.incrementAndGet();
                return super.visitFile(file, attrs);
            }
        });
        System.out.println(dirCount.get()+"\t"+fileCount.get());
    }
}

```





```java
package com.tzdphxx;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class server1test {
    public static void main(String[] args) throws IOException {


        ByteBuffer buffer = ByteBuffer.allocate(16);

        //1.创建服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();

        //改成非阻塞
        ssc.configureBlocking(false);

        //2.绑定监听端口
        ssc.bind(new InetSocketAddress(8888));

        //3.建立连接的集合
        List<SocketChannel> channels = new ArrayList<SocketChannel>();
        while(true) {
            //4.accept建立客户端的连接， SocketChannel 用来和客户端通信
            SocketChannel sc = ssc.accept();
            sc.configureBlocking(false);
            channels.add(sc);
            //5.接受客户端的数据
            for(SocketChannel ch : channels) {
                ch.read(buffer);// 没读到数据返回0
                buffer.flip();
                buffer.clear();
            }
        }
    }
}

```



处理消息的边界

- 一种思路是固定消息长度，数据包大小一样，服务器按预定长度读取，缺点是浪费带宽
- 另一种思路是按分隔符拆分，缺点是效率低
- TLV 格式，即 Type 类型、Length 长度、Value 数据，类型和长度已知的情况下，就可以方便获取消息大小，分配合适的 buffer，缺点是 buffer 需要提前分配，如果内容过大，则影响 server 吞吐量
  o Http 1.1是TLV 格式
  o Http 2.0 是LTV 格式



```java
package com.tzdphxx;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class server2 {
    public static void main(String[] args) throws IOException {


        //1.创建selector 管理多个channel
        Selector selector = Selector.open();

        ByteBuffer buffer = ByteBuffer.allocate(16);
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);


        //2。建立selector和channel的联系(SelectionKey可以知道时间和哪个channel事件)
        SelectionKey register = ssc.register(selector, 0, null);
        register.interestOps(SelectionKey.OP_ACCEPT);

        ssc.bind(new InetSocketAddress(8888));


        while (true) {
            //select没有事件就阻塞(在事件未处理时，不会阻塞)
            selector.select();
            //处理事件
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();
                if (key.isAcceptable()) {
                    ServerSocketChannel sc = (ServerSocketChannel) key.channel();
                    sc.accept();
                    sc.configureBlocking(false);
                    ByteBuffer buf = ByteBuffer.allocate(16);
                    SelectionKey scKey = sc.register(selector, 0,buf);
                    scKey.interestOps(SelectionKey.OP_READ);
                }else if(key.isReadable()){
                    try {
                        SocketChannel sc = (SocketChannel) key.channel();
                        ByteBuffer buf = (ByteBuffer) key.attachment();
                        int read = sc.read(buf);
                        if (read < 0) {
                            key.cancel();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        key.cancel();
                    }
                }

            }
        }
    }

}

```

多路复用
单线程可以配合 Selector 完成对多个 Channel 可读写事件的监控，这称之为多路复用 多路复用仅针对网络IO. 普通文件IO没法利用多路复用
如果不用 Selector 的非阻寒模式，线程大部分时间都在做无用功，而 Selector 能够保证
	有可连接事件时才去连接
	有可读事件才去读取
	有可写事件才去写入
限于网络传输能力，Channel未必时时可写，一旦Channel 可写，会触发 Selector 的可写事件





同步:线程自己去获取结果(一个线程）
异步:线程自己不去获取结果，而是由其它线程送结果(至少两个线程)





AIO 用来解决数据复制阶段的阻塞问题
同步意味着，在进行读写操作时，线程需要等待结果，还是相当于闲置
异步意味着，在进行读写操作时，线程不必等待结果，而是将来由操作系统来通过回调方式由另外的线程来获
得结果



netty入门

服务器

```java
package com.tzdphxx;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;


public class helloServer {
    public static void main(String[] args) {
        //1.启动器，负责组装netty组件，启动服务器
        new ServerBootstrap()
                //2.BossEventLoop, WorkEventLoop(selector,thread),group组
                .group(new NioEventLoopGroup())
                //3.选择服务器的ServerSocketChannel实现
                .channel(NioServerSocketChannel.class)
                //4. boos 负责处理连接 worker(child)  负责读写  决定了worker 能执行什么操作 handler
                .childHandler(
                        //5.channel代表和客户端进行数据读写的通道 Initializer 初始化 负责添加别的handler
                        new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        //6.添加具体的handle
                        ch.pipeline().addLast(new StringDecoder());//将ByteBuffer转为字符串
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {//自定义的业务处理handler
                            @Override//读事件
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                //打印上一步转换好的字符串
                                System.out.println(msg);
                            }
                        });
                    }
                })//7.绑定监听端口
                .bind(8080);
    }
}

```

客户端

```java
package com.tzdphxx;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

public class HelloClient {
    public static void main(String[] args) throws InterruptedException {
        //1.启动类
         new Bootstrap()
                 //2.添加EventLoop
                 .group(new NioEventLoopGroup())
                 //3.选择客户端channel实现
                 .channel(NioSocketChannel.class)
                 //4.添加处理器
                 .handler(new ChannelInitializer<NioSocketChannel>() {
                     @Override//在建立连接后被调用
                     protected void initChannel(NioSocketChannel ch) throws Exception {
                         ch.pipeline().addLast(new StringEncoder());
                     }
                 })
                 //5,连接到服务器
                 .connect(new InetSocketAddress("127.0.0.1", 8080))
                 .sync()//阻塞方法，知道连接建立
                 .channel()
                 //6.向服务器发送数据
                 .writeAndFlush("Hello World!");

    }
}

```

两种任务

```java
package com.tzdphxx;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.NettyRuntime;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

public class TestEventLoop {
    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();//io事件 普通任务 定时任务
        System.out.println(NettyRuntime.availableProcessors());

        //执行普通任务
        /*group.next().submit(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });*/

        //定时任务
        group.next().scheduleAtFixedRate(() -> {
            System.out.println("hello");
        },0,1, TimeUnit.SECONDS);
    }
}

```

![image-20250718212716755](C:\Users\17811\AppData\Roaming\Typora\typora-user-images\image-20250718212716755.png)
