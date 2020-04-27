#### MySQL之MVVC简介
一丶什么是MVVC？
　　MVVC (Multi-Version Concurrency Control) (注：与MVCC相对的，是基于锁的并发控制，Lock-Based Concurrency Control)是一种基于多版制协本的并发控议，只有在InnoDB引擎下存在。MVCC是为了实现事务的隔离性，通过版本号，避免同一数据在不同事务间的竞争，你可以把它当成基于多版本号的一种乐观锁。当然，这种乐观锁只在事务级别未提交锁和已提交锁时才会生效。MVCC最大的好处，相信也是耳熟能详：读不加锁，读写不冲突。在读多写少的OLTP应用中，读写不冲突是非常重要的，极大的增加了系统的并发性能。具体见下面介绍。,,     

二丶MVVC的实现机制
　　InnoDB在每行数据都增加两个隐藏字段，一个记录创建的版本号，一个记录删除的版本号。

　　在多版本并发控制中，为了保证数据操作在多线程过程中，保证事务隔离的机制，降低锁竞争的压力，保证较高的并发量。在每个开启一事务时，会生成一个事务的版本号，被操作的数据会生成一条新的数据行（临时），但是在提交前对其他事务是不可见的，对于数据的更新（包括增删改）操作成功，会将这个版本号更新到数据的行中，事务提交成功，将新的版本号更新到此数据行中，这样保证了每个事务操作的数据，都是互不影响的，也不存在锁的问题。
三丶MVVC下的CRUD
SELECT：
　　当隔离级别是REPEATABLE READ时select操作，InnoDB必须每行数据来保证它符合两个条件：
　　1、InnoDB必须找到一个行的版本，它至少要和事务的版本一样老(也即它的版本号不大于事务的版本号)。这保证了不管是事务开始之前，或者事务创建时，或者修改了这行数据的时候，这行数据是存在的。
　　2、这行数据的删除版本必须是未定义的或者比事务版本要大。这可以保证在事务开始之前这行数据没有被删除。
符合这两个条件的行可能会被当作查询结果而返回。


INSERT：

　　InnoDB为这个新行记录当前的系统版本号。
DELETE：

　　InnoDB将当前的系统版本号设置为这一行的删除ID。
UPDATE：

　　InnoDB会写一个这行数据的新拷贝，这个拷贝的版本为当前的系统版本号。它同时也会将这个版本号写到旧行的删除版本里。

　　这种额外的记录所带来的结果就是对于大多数查询来说根本就不需要获得一个锁。他们只是简单地以最快的速度来读取数据，确保只选择符合条件的行。这个方案的缺点在于存储引擎必须为每一行存储更多的数据，做更多的检查工作，处理更多的善后操作。

　　MVCC只工作在REPEATABLE READ和READ COMMITED隔离级别下。READ UNCOMMITED不是MVCC兼容的，因为查询不能找到适合他们事务版本的行版本；它们每次都只能读到最新的版本。SERIABLABLE也不与MVCC兼容，因为读操作会锁定他们返回的每一行数据。

https://blog.csdn.net/Butterfly_resting/article/details/89704636


#### 聚集索引与非聚集索引的区别
MyisAM和innodb的有关索引的疑问
两者都是什么索引？聚集还是非聚集https://www.cnblogs.com/olinux/p/5217186.html
MyISAM（ 非聚集）
使用B+Tree作为索引结构，叶节点的data域存放的是数据记录的地址。
MyISAM中索引检索的算法为首先按照B+Tree搜索算法搜索索引，如果指定的Key存在，则取出其data域的值，然后以data域的值为地址，读取相应数据记录。
InnoDB（ 聚集索引）
第一个重大区别是InnoDB的数据文件本身就是索引文件， 这棵树的叶节点data域保存了完整的数据记录。
但是辅助索引搜索需要检索两遍索引：首先检索辅助索引获得主键，然后用主键到主索引中检索获得记录。

因为InnoDB的数据文件本身要按主键聚集，所以InnoDB要求表必须有主键（MyISAM可以没有）
1）如果没有显式指定，则MySQL系统会自动选择一个可以唯一标识数据记录的列作为主键
2）如果不存在这种列，则MySQL自动为InnoDB表生成一个隐含字段作为主键，这个字段长度为6个字节，类型为长整形。（隐含字段）
简单说：
如果我们定义了主键(PRIMARY KEY)，那么InnoDB会选择其作为聚集索引；如果没有显式定义主键，则InnoDB会选择第一个不包含有NULL值的唯一索引作为主键索引；

##### 说说分库与分表设计（面试过）分片
分库与分表的目的在于，减小数据库的单库单表负担，提高查询性能，缩短查询时间。
通过分表，可以减少数据库的单表负担，将压力分散到不同的表上，同时因为不同的表上的数据量少了，起到提高查询性能，缩短查询时间的作用，此外，可以很大的缓解表锁的问题。
分表策略可以归纳为垂直拆分和水平拆分。
水平分表：取模分表就属于随机分表，而时间维度分表则属于连续分表。
如何设计好垂直拆分，我的建议：将不常用的字段单独拆分到另外一张扩展表. 将大文本的字段单独拆分到另外一张扩展表, 将不经常修改的字段放在同一张表中，将经常改变的字段放在另一张表中。
对于海量用户场景，可以考虑取模分表，数据相对比较均匀，不容易出现热点和并发访问的瓶颈。

库内分表，仅仅是解决了单表数据过大的问题，但并没有把单表的数据分散到不同的物理机上，因此并不能减轻 MySQL 服务器的压力，仍然存在同一个物理机上的资源竞争和瓶颈，包括 CPU、内存、磁盘 IO、网络带宽等。

分库与分表带来的分布式困境与应对之策
数据迁移与扩容问题----一般做法是通过程序先读出数据，然后按照指定的分表策略再将数据写入到各个分表中。
分页与排序问题----需要在不同的分表中将数据进行排序并返回，并将不同分表返回的结果集进行汇总和再次排序，最后再返回给用户。
分布式全局唯一ID—UUID、GUID等



#### innodb为什么要用自增id作为主键：
如果表使用自增主键，那么每次插入新的记录，记录就会顺序添加到当前索引节点的后续位置，当一页写满，就会自动开辟一个新的页
如果使用非自增主键（如果身份证号或学号等），由于每次插入主键的值近似于随机，因此每次新纪录都要被插到现有索引页得中间某个位置， 频繁的移动、分页操作造成了大量的碎片，得到了不够紧凑的索引结构，后续不得不通过OPTIMIZE TABLE（optimize table）来重建表并优化填充页面。
————————————————


#### 事务四大特性（ACID）原子性、一致性、隔离性、持久性？
原子性：一个事务（transaction）中的所有操作，要么全部完成，要么全部不完成，不会结束在中间某个环节。
。事务在执行过程中发生错误，会被恢复（Rollback）到事务开始前的状态，就像这个事务从来没有执行过一样。
一致性：在事务开始之前和事务结束以后，数据库的完整性没有被破坏。这表示写入的资料必须完全符合所有的预设规则，这包含资料的精确度、串联性以及后续数据库可以自发性地完成预定的工作。
隔离性：数据库允许多个并发事务同时对其数据进行读写和修改的能力，隔离性可以防止多个事务并发执行时由于交叉执行而导致数据的不一致。事务隔离分为不同级别，包括读未提交（Read uncommitted）、读提交（read committed）、可重复读（repeatable read）和串行化（Serializable）。
持久性：事务处理结束后，对数据的修改就是永久的，即便系统故障也不会丢失。


####  MySQL常见的存储引擎InnoDB、MyISAM的区别？
1）事务：MyISAM不支持，InnoDB支持
2）锁级别： MyISAM 表级锁，InnoDB 行级锁及外键约束
（MySQL表级锁有两种模式：表共享读锁（Table Read Lock）和表独占写锁（Table Write Lock）。什么意思呢，就是说对MyISAM表进行读操作时，它不会阻塞其他用户对同一表的读请求，但会阻塞对同一表的写操作；而对MyISAM表的写操作，则会阻塞其他用户对同一表的读和写操作。
InnoDB行锁是通过给索引项加锁来实现的，即只有通过索引条件检索数据，InnoDB才使用行级锁，否则将使用表锁！行级锁在每次获取锁和释放锁的操作需要消耗比表锁更多的资源。在InnoDB两个事务发生死锁的时候，会计算出每个事务影响的行数，然后回滚行数少的那个事务。当锁定的场景中不涉及Innodb的时候，InnoDB是检测不到的。只能依靠锁定超时来解决。）
3）MyISAM存储表的总行数；InnoDB不存储总行数；
4）MyISAM采用非聚集索引，B+树叶子存储指向数据文件的指针。InnoDB主键索引采用聚集索引，B+树叶子存储数据

适用场景：
MyISAM适合： 插入不频繁，查询非常频繁，如果执行大量的SELECT，MyISAM是更好的选择， 没有事务。
InnoDB适合： 可靠性要求比较高，或者要求事务； 表更新和查询都相当的频繁， 大量的INSERT或UPDATE




#### CAP 原则

CAP原则又称CAP定理，指的是在一个分布式系统中，一致性（Consistency）、可用性（Availability）、分区容错性（Partition tolerance）。CAP 原则指的是，这三个要素最多只能同时实现两点，不可能三者兼顾。

一致性（C）：在分布式系统中的所有数据备份，在同一时刻是否同样的值。

可用性（A）：在集群中一部分节点故障后，集群整体是否还能响应客户端的读写请求。

分区容忍性（P）：以实际效果而言，分区相当于对通信的时限要求。系统如果不能在时限内达成数据一致性，就意味着发生了分区的情况，必须就当前操作在C和A之间做出选择。


四、哈希索引的优势：

等值查询，哈希索引具有绝对优势（前提是：没有大量重复键值，如果大量重复键值时，哈希索引的效率很低，因为存在所谓的哈希碰撞问题。）

五、哈希索引不适用的场景：

不支持范围查询

不支持索引完成排序

不支持联合索引的最左前缀匹配规则



十七、关于MVVC

MySQL InnoDB存储引擎，实现的是基于多版本的并发控制协议——MVCC (Multi-Version Concurrency Control) 

注：与MVCC相对的，是基于锁的并发控制，Lock-Based Concurrency Control

MVCC最大的好处：读不加锁，读写不冲突。在读多写少的OLTP应用中，读写不冲突是非常重要的，极大的增加了系统的并发性能，现阶段几乎所有的RDBMS，都支持了MVCC。

LBCC：Lock-Based Concurrency Control，基于锁的并发控制

MVCC：Multi-Version Concurrency Control

基于多版本的并发控制协议。纯粹基于锁的并发机制并发量低，MVCC是在基于锁的并发控制上的改进，主要是在读操作上提高了并发量。

十八、在MVCC并发控制中，读操作可以分成两类：

快照读 (snapshot read)：读取的是记录的可见版本 (有可能是历史版本)，不用加锁（共享读锁s锁也不加，所以不会阻塞其他事务的写）

当前读 (current read)：读取的是记录的最新版本，并且，当前读返回的记录，都会加上锁，保证其他事务不会再并发修改这条记录


https://www.cnblogs.com/williamjie/p/11081592.html


https://blog.csdn.net/qq_38538733/article/details/88902979



任务
面试官提出的问题将出现在这里。

标题：服务循环依赖检测
描述信息
在微服务的架构下，公司内部会有非常多的独立服务。

服务之间可以相互调用，往往大型应用调用链条很长，如果出现循环依赖将出现非常恶劣的影响。

对于一个具体应用，已知各个服务的调用关系（即依赖关系），请判断是否存在循环调用。

输入：

一组服务依赖关系list，('A', 'B') 表示 A 会调用 B 服务

service_relations = [('A', 'B'), ('A', 'C'), ('B', 'D'), ('D', 'A')]

输出：

由于存在 A - B - D - A 故存在循环依赖，返回True；反之如果不存在，返回False

Follow up：

1. 如果有多个环，请都检测出来

2. 返回每个环中的服务名





https://www.toutiao.com/i6625193267141050893/?group_id=6625193267141050893

https://www.toutiao.com/a6659621973355659788/


#版本链
https://mp.weixin.qq.com/s?__biz=MzU4NjQwNTE5Ng==&mid=2247483681&idx=1&sn=03adfb89521568013f6a1efd9ca1af6a&scene=21#wechat_redirect



https://blog.csdn.net/u010002184/article/details/88526708


https://mp.weixin.qq.com/s?__biz=MzU4NjQwNTE5Ng==&mid=2247483681&idx=1&sn=03adfb89521568013f6a1efd9ca1af6a&scene=21#wechat_redirect
innodb_flush_log_at_trx_commit和sync_binlog  



innodb_flush_log_at_trx_commit:

0: 由mysql的main_thread每秒将存储引擎log buffer中的redo日志写入到log file，并调用文件系统的sync操作，将日志刷新到磁盘。
1：每次事务提交时，将存储引擎log buffer中的redo日志写入到log file，并调用文件系统的sync操作，将日志刷新到磁盘。
2：每次事务提交时，将存储引擎log buffer中的redo日志写入到log file，并由存储引擎的main_thread 每秒将日志刷新到磁盘。


sync_binlog：

0 ：存储引擎不进行binlog的刷新到磁盘，而由操作系统的文件系统控制缓存刷新。
1：每提交一次事务，存储引擎调用文件系统的sync操作进行一次缓存的刷新，这种方式最安全，但性能较低。
n：当提交的日志组=n时，存储引擎调用文件系统的sync操作进行一次缓存的刷新。


https://www.sohu.com/a/340423086_744669

从innodb的索引结构分析，为什么索引的key长度不能太长
https://www.jianshu.com/p/bea78290722c




mysqlbinlog 恢复数据到任意时间点
http://blog.itpub.net/31559358/viewspace-2221403/

https://www.cnblogs.com/john5yang/p/10518457.html

https://www.cnblogs.com/zero-gg/p/9057092.html


RDS 低成本

MHA（Master HA）

MHA工作原理总结为以下几条：
（1） 从宕机崩溃的 master 保存二进制日志事件（binlog events）；
（2） 识别含有最新更新的 slave ；
（3） 应用差异的中继日志(relay log) 到其他 slave ；
（4） 应用从 master 保存的二进制日志事件(binlog events)；
（5） 提升一个 slave 为新 master ；
（6） 使用其他的 slave 连接新的 master 进行复制。



Mysql高可用方案

https://www.jianshu.com/p/cdec2019dc88

MGR
MySQL官方推荐的一款高可用集群方案MySQL Group Replication，基于Paxos协议的状态机复制，彻底解决了基于传统的异步复制和半同步复制中数据一致性问题无法保证的情况，也让MySQL数据库涉及的领域更广，打开互联网金融行业的大门。



PXC
Percona XtraDB Cluster是MySQL高可用性和可扩展性的解决方案
1).同步复制，事务要么在所有节点提交或不提交。
2).多主复制，可以在任意节点进行写操作。
3).在从服务器上并行应用事件，真正意义上的并行复制。
4).节点自动配置。
5).数据一致性，不再是异步复制。


Galera协议
https://blog.csdn.net/L835311324/article/details/89345195
#异步复制
传统的数据主从辅助属于异步复制，从库起IO线程连接主库，获取主库二进制日志写到本地中继日志，并更新master-info文件（存放主库相关信息），从库再利用SQL线程执行中继日志。
 

#半同步复制
半同步复制则当确认了从库把二进制日志写入中继日志才会允许提交
ASK 

二段提交
三段提交

#一致性算法（paxos，raft）
三节点企业版，MGR



#并行复制
官方 MySQL5.6 版本，支持了并行复制，只是支持的粒度是按库并行。
优点：实现逻辑简单，binlog格式同时支持statement和row。




基础版
高可用版本
集群版
三节点企业版 raft协议



GTID
https://www.cnblogs.com/caicz/p/10855605.html

Twitter的ID生成算法snowflake
https://www.cnblogs.com/relucent/p/4955340.html


一致性算法
https://www.bilibili.com/video/BV19t411c75p?p=2


raft在线演示地址
http://thesecretlivesofdata.com/raft/

MYSQL锁
行锁，gap锁
https://blog.csdn.net/yajie_12/article/details/79972194
https://www.cnblogs.com/crazylqy/p/7773492.html

MYSQL 意向锁
https://www.cnblogs.com/crazylqy/p/7773492.html
https://www.zhihu.com/question/51513268



#半同步复制
https://www.cnblogs.com/zero-gg/p/9057092.html
https://www.sohu.com/a/326064410_487483

现在我们已经知道，在半同步环境下，主库是在事务提交之后等待Slave ACK，所以才会有数据不一致问题。所以这个Slave ACK在什么时间去等待，也是一个很关键的问题了。因此MySQL针对半同步复制的问题，在5.7.2引入了Loss-less Semi-Synchronous，在调用binlog sync之后，engine层commit之前等待Slave ACK。这样只有在确认Slave收到事务events后，事务才会提交。在commit之前等待Slave ACK，同时可以堆积事务，利于group commit，有利于提升性能。


rpl_semi_sync_master_enable=ON
rpl_semi_sync_master_time=10000
rpl_semi_sync_master_wait_point = after_commit , after_sync
rpl_semi_sync_master_wait_slave_count = 1

https://www.sohu.com/a/326064410_487483



MySQL数据库InnoDB存储引擎在线加字段实现原理详解
http://www.zhdba.com/mysqlops/2013/09/14/mysql-innodb-online-ddl/

Mysql自适应索引
Mysql审计插件
http://tools.percona.com/

Mysql double write page checksum
RAID 0 ~ 3

set profiling=1;
select * from user limit 10;

show profile for query 1;

select * from information_schema.profiling where QUERY_ID=1
order by duration desc
;

show status where variable_name like '%Handler%';

show global status where variable_name like '%Thread%';


show processlist;

show innodb status;

show master status;

show tables from information_schema like '%_STAT%'

select * from  information_schema.INNODB_BUFFER_POOL_STATS

show status like '%last%'

show variables like '%innodb%'

SHOW ENGINE INNODB STATUS ;



