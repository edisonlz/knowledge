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


