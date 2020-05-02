1. kafka是如何实现高并发的
顺序写 + zero copy
(https://blog.csdn.net/weixin_43975220/article/details/93190906)
https://blog.csdn.net/u010039929/article/details/62428360
https://www.iteblog.com/archives/2227.html

2. kafka 是如何进行选主的
   分布式锁
   当ISR中的个副本的LEO不一致时，如果此时leader挂掉，选举新的leader时并不是按照LEO的高低进行选举，而是按照ISR中的顺序选举。


3. kafka如何确保可靠性
在kafka中，有一个acks参数可以来设置，这个参数表示必须有多少个分区副本收到消息，Kafka才会认为写入成功。可以有三个选项来进行设置：
acks=0，这种方式表示生产者不会等待服务器是否收到消息的答复，也就是说即使由于网络问题服务器没有收到消息，生产者也不会知道，这种方式适合用于数据要求不高的场合，其性能很高。

acks=1，这种方式表示生产者需要等待接收主节点的应答消息才会知道数据写入成功，如果没有收到应答信息或者当前集群因为选举没有主节点，此时会抛出异常。但是如果一台节点没有收到消息成为了主节点，此时会数据还是会丢失。

acks=all，这种方式表示生产者需要收到所有服务器的应答信息，才会知道消息发送成功，这种方式最为安全，但是其吞吐量低。

producer 级别：acks=all（或者 request.required.acks=-1），同时发生模式为同步 producer.type=sync
topic 级别：设置 replication.factor>=3，并且 min.insync.replicas>=1；
broker 级别：关闭不完全的 Leader 选举，即 unclean.leader.election.enable=false

https://www.toutiao.com/a6746084878120387080/


Kafka的ISR机制保证了ISR列表中至少leader写入数据成功并且至少有一个follower同步完成leader的数据，才会认为消息发送成功，否则会认为发送失败，一直重试。


想要开启这个特性，获得每个分区内的精确一次语义，也就是说没有重复，没有丢失，并且有序的语义，只需要 producer 配置 enable.idempotence=true。
https://blog.csdn.net/w1992wishes/article/details/89502956
这个特性是怎么实现的呢？每个新的 Producer 在初始化的时候会被分配一个唯一的 PID，该PID对用户完全透明而不会暴露给用户。在底层，它和 TCP 的工作原理有点像，每一批发送到 Kafka 的消息都将包含 PID 和一个从 0 开始单调递增序列号。



3. 深入理解Kafka数据高并发写入、可靠性以及EOS语义
https://www.toutiao.com/a6732706848916374030/


4. 消息的幂等性
三、EOS语义
所谓EOS语义指 Exactly Once语义，表示数据有且仅被处理一次，在常见的流数据处理场景中，都存在着这种问题（画外音：在spark的低版本中并不能保证这种语义，Flink中可以保证该语义）。

PID
sequence += 1


https://www.bilibili.com/video/BV1iE411t7cm?p=2

redis CAP理论： 
https://www.jianshu.com/p/c01e81eaa9ab


#基本数据结构
https://www.toutiao.com/a6758731417548489229/
https://www.toutiao.com/a6582089108129055245/



#参数配置说明
https://www.cnblogs.com/rilley/p/5391268.html

#kafka详细说明
https://blog.csdn.net/u013256816/article/details/71091774

https://blog.csdn.net/define_us/article/details/83086358




#kafka选主机制
羊群效应
https://blog.csdn.net/qq_27384769/article/details/80115392
https://www.cnblogs.com/tonggc1668/p/12051030.html

#选择分区算法 fail over机制
https://www.ixigua.com/i6814398124530860559/
https://www.bilibili.com/video/BV1iE411t7cm?p=6
https://www.bilibili.com/video/BV1iE411t7cm?p=6



#测试

启动zookeeper
../bin/zookeeper-server-start.sh ./zookeeper.properties 

启动kafka0
../bin/kafka-server-start.sh ./server0.properties 

启动kafka1
../bin/kafka-server-start.sh ./server1.properties 

启动kafka2
../bin/kafka-server-start.sh ./server2.properties 


创建topic
../bin/kafka-topics.sh --zookeeper localhost:2181 --topic t1 --create --partitions 3 --replication-factor 2

../bin/kafka-topics.sh --zookeeper localhost:2181 --topic t1 --describe
../bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic t1 --from-beginning
../bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test --from-beginning --partition 0
../bin/kafka-console-producer.sh --broker-list localhost:9092 --topic t1

列出所有topic
../bin/kafka-topics.sh --list --zookeeper localhost:2181

java kafka 代码
http://www.luyixian.cn/news_show_185458.aspx





##### 相关文档
https://www.toutiao.com/a6732206736918184459/
http://kafka.apachecn.org/documentation.html#design_constanttime

https://blog.csdn.net/weixin_43975220/article/details/93190906
https://www.iteblog.com/archives/2227.html
https://www.iteblog.com/archives/2227.html
