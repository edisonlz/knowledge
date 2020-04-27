
elastic

https://www.toutiao.com/a6694151176931246595/


https://www.jianshu.com/p/e564d9f4e663


#elastic 问题
https://www.toutiao.com/a6774336843535090188/


倒排索引，相反于一篇文章包含了哪些词，它从词出发，记载了这个词在哪些文档中出现过，由两部分组成——词典和倒排表。

由两部分组成——词典和倒排表。
加分项：倒排索引的底层实现是基于：FST（Finite State Transducer）数据结构。
lucene 从 4+版本后开始大量使用的数据结构是 FST。FST 有两个优点：
（1）空间占用小。通过对词典中单词前缀和后缀的重复利用，压缩了存储空间；
（2）查询速度快。O(len(str))的查询时间复杂度。


