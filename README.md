# java-study
学习java各种框架和知识的记录
## 01-shiro
shiro是目前主流的java安全框架，主要用来更便捷的认证，授权，加密，会话管理。

对应博客文章: https://blog.csdn.net/sqlgao22/article/details/98506479

## 02-token
JWT是为了网络应用环境间传递声明而执行的一种基于JSON的开发标准，该token被设计为紧凑且安全的，特别适用于分布式站点的单点登陆（SSO）场景。

对应博客文章: https://blog.csdn.net/sqlgao22/article/details/98532943

## 03-shiroandtoken
使用shiro和token实现验证身份以及登录验证.

对应博客文章: https://blog.csdn.net/sqlgao22/article/details/99186391

## 04-dbtoexcel01
将数据库数据导出为excel数据(用的mybatis+poi)
这种方式通过常规的mybatis将数据库中数据转为实体类,然后将实体类转换为map.
最后将map中的内容写入excel.
#### 使用的工具是apach poi :
1. Apache POI 是用Java编写的免费开源的跨平台的 Java API，Apache POI提供API给Java程式对Microsoft Office格式档案读和写的功能。

2. POI框架的类库：
- HSSF － 提供读写Microsoft Excel格式档案的功能。
- XSSF － 提供读写Microsoft Excel OOXML格式档案的功能。
- HWPF － 提供读写Microsoft Word格式档案的功能。
- HSLF － 提供读写Microsoft PowerPoint格式档案的功能。

对应博客文章:https://blog.csdn.net/sqlgao22/article/details/99618283

## 05-dbtoexcel02
将数据库的数据转为excel,目的同上一个.但是思路不同.这种方式使用的jdbc+list封装单元格
使用起来较复杂,但是效率较高.

对应博客文章:https://blog.csdn.net/sqlgao22/article/details/100736325

## 06-exceltodb
与上面两个相反,这个是将excel表中的数据导入数据库.
将表的表头以数据库字段注释的方式存入.同时控制读取的excel表格中的行数.
与  05-dbtoexcel02  是一套.

对应的博客文章:https://blog.csdn.net/sqlgao22/article/details/100082382

## 07-quartz
quartz是一个通过db实现的分布式的定时任务的调度框架.

对应的博客文章:https://blog.csdn.net/sqlgao22/article/details/100669377
quartz表字段的解释:https://blog.csdn.net/sqlgao22/article/details/100697214

## 08-ehcache
Ehcache是一个本地缓存的框架.Ehcache 是现在最流行的纯Java开源缓存框架，配置简单、结构清晰、功能强大.在使用需要缓存时可以使用.使用起来简单方便.支持多种缓存策略.

对应博客文章:https://blog.csdn.net/sqlgao22/article/details/102462898

## 09-pagehelper
pagehelper是一个开源的分页的帮助框架.让代码中使用分页更加的方便.

对应博客文章:https://blog.csdn.net/sqlgao22/article/details/102467386

## 10-tkmybatis
tkmybatis是对mybatis进一步的封装,通过集成tkmybatis接口的方式.可以使用一些简单的增删改查.复杂的连表查询页可以写SQL语句.
不过个人认为,使用mybatis的逆向工程同样可以做到自动封装简单的SQL.

对应的博客文章:https://blog.csdn.net/sqlgao22/article/details/102502966

## 11-thread
学习了一些有关线程的知识.
- 线程有关知识点
- 使用适配器模式
- 捕获线程池中线程的异常
- 线程的创建方式
- 线程安全的单例
- 线程之间的通信
- 生产者和消费者代码
- 多线程打印abc

对应的博客文章:https://blog.csdn.net/sqlgao22/article/details/100739505

## 12-redis
使用redisTemplate以及使用Jedis客户端连接连接redis.
同时区分了redis的集群和单机连接区别.

## 13-juc
学习java.util.concurrent这个并发包,简称juc包.整个juc包基于cas操作构建.
- 学习原子类.线程安全的操作对象,数组,集合以及基本类型
https://blog.csdn.net/sqlgao22/article/details/102585605
- 学习并发工具类.线程等待,交换数据,阻塞等工具类的操作.
https://blog.csdn.net/sqlgao22/article/details/102587675
- 学习juc的锁/读写锁以及condition条件类
https://blog.csdn.net/sqlgao22/article/details/102587423
- 学习juc中线程安全的集合类,包括线程安全的list,set,map,queue,dequeue
https://blog.csdn.net/sqlgao22/article/details/102611839
- 学习DelayQueue延时队列,处理过期的订单
https://blog.csdn.net/sqlgao22/article/details/102621098
- 学习线程池.
https://blog.csdn.net/sqlgao22/article/details/102622457
- 学习ForkJoinPool可以分解合并的任务
https://blog.csdn.net/sqlgao22/article/details/102664406

## 14-distributelock
学习分布式锁
1. 使用db完成分布式锁
- 使用数据库乐观锁

2. 使用redis完成分布式锁
- 使用jedis客户端自行实现
- 使用redisson已经封装好的redlock

3. 使用zookeeper实现分布式锁
- 使用curatorFramework框架封装好的zk
- 使用zkclient自己实现zk锁

对应的博客文章:https://blog.csdn.net/sqlgao22/article/details/102802351

## 15-socket
学习使用socket编程.
socket是一个网络编程的模型,是最基础的java网络.

对应的博客文章:https://blog.csdn.net/sqlgao22/article/details/102858119

## 16-listener
使用一下spring封装的监听者模式

对应的博客文章:https://blog.csdn.net/sqlgao22/article/details/102394761

## 17-netty
该项目中有两个方面:
1. nio:一种同步无阻塞的通信方式.
2. netty:是一个高性能、异步事件驱动的 NIO 框架.

对应nio文章:https://blog.csdn.net/sqlgao22/article/details/103087676

对应netty文章:https://blog.csdn.net/sqlgao22/article/details/103146599

## 18-docker
使用docker发布容器,已经使用docker-compose进行容器的编排部署

对应文章:https://blog.csdn.net/sqlgao22/article/details/103487790


## freemarker
使用freemarker生成代码.
生成代码 -> 代码文件
生成代码 -> 字节流压缩直接传送给客户端
压缩 -> 压缩文件
压缩 -> 使用byte[]直接传流给客户端

对应文章:https://blog.csdn.net/sqlgao22/article/details/106298887











