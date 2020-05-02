#### 6. 对象分配过程
 - 逃逸分析
 - 标量替换

![multi](./imgs/newobject.png)

是否能能在栈上分配 

yes: 在线程栈分配，生命周期结束，出栈
no: 检查 TLHB Thread Local Heap Buffer 是否够用

yes： 不用加锁，直接分配内存
no: 在堆上竞争内存分配，需要上锁


##### 对象
引用计数器
GCROOT
##### 垃圾回收算法：
标记-清除:位置不连续，产生碎片
拷贝: 内存连续，浪费空间
标记-整理：效率低

##### 垃圾回收期分代：
新生代 + 老年代 + 永久代1.7/元数据空间 metaspace 1.8 
1. 永久代 元数据 Class
2. 永久代必须指定大小（永久代溢出），元数据不用
3. string 1.7 永久代， 1.8 堆
4. method area

https://www.bilibili.com/video/BV1xK4y1C7aT?from=search&seid=3214376045616592807
https://www.bilibili.com/video/BV1oK411V7vE/?spm_id_from=333.788.videocard.1
https://www.bilibili.com/video/BV1xK4y1C7aT?p=4



##### JVM学习视频
https://www.bilibili.com/video/BV1w5411t7yH/?spm_id_from=333.788.videocard.6




![jvmgc](./imgs/jvmgc.png)

1. 垃圾回收期的演讲路线，伴随着内存约来越大的演讲
从分代算法演进到非分代算法
Serial算法 几十兆
Parallel算法 几个G
CMS算法 十个G - 三色标记算法
 - 三色标记 - 错标 - incremental update - remark 

![g1](./imgs/g1.png)
G1算法可以支持上百G内存(Garbage First)，逻辑分代，物理部分代
 - 三色标记  + SATB
ZGC - Shenandoah -- 4T
（C4）
 - coloredPointers(着色指针)


1. JDK诞生Serial追随提高效率，诞生PS,为了配合CMS，诞生了PN，CMS是1.4版本后期引入，CMS是里程碑的GC，他开启了并发回收的过程，但CMS毛病较多，因此目前任何一个JDK版本默认的是CMS并发垃圾回收是因为，无法忍受STW
2. Serial年轻代串行回收
3. PS年轻代并行回收


###### 概念 YGC FGC
1. YGC
  - yong GC Minor GC
  - Eden 区不足 
2. FGC
  - Ful GC Major GC
  - Old 空间不足
  - System.gc()


##### 垃圾回收器
1. Serial 
a stop the world , copying colletor with a single gc thread
2. Serial Old
a stop the world, mark-sweep-compact collector that uses a single GC thread.
3. Parallel Scavenge
a stop-the-world,copying collector which uses multiple GC threads.
4. Parallel Old
a stop-the-world,mark-sweep-compact collector which uses multiple GC threads.

5. CMS
Concurrent Mark-Sweep 和工作线程一起并行工作
并发标记，STW重新标记，并发清理

6. ParNew
 - a stop the world ,copying collector which
uses multiple GC threads
 - it differ from "Parallel Scavenge" in that it has enhancements that make it usable with CMS.
 - for example "ParNew" does the Synchronizaztionn needed so that it can run during the concurrent phases of CMS.
  
  
1.8  PS + PO

```java
$ java -XX:+PrintCommandLineFlags  -version

-XX:InitialHeapSize=268435456 -XX:MaxHeapSize=4294967296 -XX:+PrintCommandLineFlags -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseParallelGC 
java version "1.8.0_151"
Java(TM) SE Runtime Environment (build 1.8.0_151-b12)
Java HotSpot(TM) 64-Bit Server VM (build 25.151-b12, mixed mode)

```

 


#### JVM调优前的基础概念
1. 吞吐量： 用户代码时间/（用户代码执行时间+垃圾回收时间）
2. 响应时间：STW越短，响应时间越好

所谓调优，首先确定，追求啥？吞吐量优先，还是响应时间优先？还是满足一定的响应时间的情况下，要达到多大的吞吐量。
问题：
科学计算，吞吐量，数据挖掘，thrput。吞吐量优先的一般：(PS+PO)
响应时间：网站 GUI API （1.8 G1）

#### 什么是调优？
1. 根据需要进jvm规划和预调优
2. 优化运行jvm运行环境（卡顿）
3. 解决jvm运行过程中出现的各种OOM问题

#### 调优，从规划开始
- 调优，从业务场景开始，没有业务场景的调优都是耍流氓。
- 无监控（压力测试），不调优
- 步骤
  1. 熟悉业务场景（没有最好的垃圾回收器，只有最适合的）


#### JVM调优第一步，了解JVM常用命令行参数
- HotSpot参数分类
  标准： - 开头
  非标准： -X 开头
  不稳定： -XX 开头

java
java -X 
```java
-Xms<size>        设置初始 Java 堆大小
-Xmx<size>        设置最大 Java 堆大小
```
java -XX
java -XX:+PrintFlagsWithComments //只有debug版本能用
java -XX:+PrintFlagsFinal
java -XX:+PrintFlagsInitial


##### GC 常用参数
-Xmn -Xms -Xmx -Xss
年轻代，最小堆 最大堆 栈空间
-XX:+UseTLAB
使用TLAB，默认打开
-XX:+PrintTLAB
-XX:TLABSize
设置TLAB大小
-XX:+DisableExplictGC
禁用 System.gc()，FGC
-XX:+PrintGC
-XX:+PrintGCDetials
-XX:+PrintHeapAtGC
-XX:+PrintGCTimeStamps
-XX:+PrintGCApplicationnConcurrentTime
打印应用程序开始时间
-XX:+PrintGCApplicationnStopedTime
打印应用程序暂停时间
-XX:+PrintReferenceGC
记录回收了多少种不同类型的引用
-verbose:class
类加载详细过程

##### Parallel 常用参数
-XX:SuvivorRatio
-XX:PerTenureSizeThreshold
大对象多大
-XX:MaxTenuringThreshold
-XX:+ParallelGCThreads
并行收集器的线程数，同样适用于CMS，一般和CPU Core数相同
-XX:+UseAdaptiveSizePolicy
自动选择各区大小比例
##### CMS  常用参数
-XX:+UseConcMarkSweepGC
-XX:ParallelCMSThreads
CMS线程数量
-XX:CMSInitiatingOccupancyFraction
使用多少比例的老年代后开始CMS收集，默认68%。
-XX:+UseCMSCompactAtFullCollection
在FGC之后进行压缩
-XX:CMSFullGCsBeforeCompaction
多少次FGC之后进行压缩
-XX:+CMSClassUnloadingEnabled
-XX:CMSInitiatingPermOccupancyFraction
达到什么比例进行Perm回收
GCTimeRatio
设置GC时间占用程序的运行时间的百分比
-XX:MaxGCPauseMillis
停顿时间
##### G1 常用参数
-XX: +UseG1GC
-XX:MaxGCPauseMillis
-XX:GCPauseIntervalMillis
GC间隔时间
-XX:+G1HeapRegionSize
分区大小，建议逐渐增大该值，1 2 4 8 16 32 
随着size增大，垃圾的存活时间更长，GC间隔更长，但每次GC时间也会更长。
ZGC做了改进（动态区块大小）

G1NewSizePercent
新生代最小比例默认5%
G1MaxNewSizePercent
新生代最大比例默认60%
GCTimeRatio
GC时间建议比例，G1会跟进这个值调整堆空间
ConcGCThreads
GC线程数量
InitiatingHeapOccupancyPercent
启动G1的堆空间占用比例



#图形界面-本地调优
visualvm

一般用：arthas 工具做jvm调优
https://alibaba.github.io/arthas/install-detail.html
JVMTI


#### CMS方案：Incremental Update产生的问题
并发标记产生漏标
1. ![cmsiu](./imgs/cmsiu.png)
所以，CMS的remark阶段，必须从头扫描一遍
2. ![g1satb](./img/g1satb.png)
https://www.jianshu.com/p/548c67aa1bc0

3. zgc
![zgc](./img/zgc.png)
https://www.jianshu.com/p/4e4fd0dd5d25
