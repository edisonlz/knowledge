此篇文章不是原创，内容来自于《深入理解java虚拟机》。仅作笔记使用。如有错误之处，请不吝指正，谢谢。

在介绍之前，首先简单说一下堆内存的结构
堆内存在大的结构上分为：年轻代和年老代。其中年轻代又分为Eden区和Survivor区。Survivor区又分为两个相等的区域，一个是fromspace区，另外一个是tospace区。年轻代内存=Eden+其中一个Survivor区，也就是说两个Survivor区，虚拟机只使用了其中一个。

Minor GC：简单理解就是发生在年轻代的GC。
Minor GC的触发条件为：当产生一个新对象，新对象优先在Eden区分配。如果Eden区放不下这个对象，虚拟机会使用复制算法发生一次Minor GC，清除掉无用对象，同时将存活对象移动到Survivor的其中一个区(fromspace区或者tospace区)。虚拟机会给每个对象定义一个对象年龄(Age)计数器，对象在Survivor区中每“熬过”一次GC，年龄就会+1。待到年龄到达一定岁数(默认是15岁)，虚拟机就会将对象移动到年老代。如果新生对象在Eden区无法分配空间时，此时发生Minor GC。发生MinorGC，对象会从Eden区进入Survivor区，如果Survivor区放不下从Eden区过来的对象时，此时会使用分配担保机制将对象直接移动到年老代。

Major GC的触发条件：Major GC又称为Full GC。当年老代空间不够用的时候，虚拟机会使用“标记—清除”或者“标记—整理”算法清理出连续的内存空间，分配对象使用。


GC

https://www.bilibili.com/video/BV1dt411u7wi?from=search&seid=14834578510088621508




https://www.cnblogs.com/Ti1077/p/9498507.html