https://blog.csdn.net/loushuiyifan/article/details/84339151

一提到元素查找，我们会很自然的想到HashMap。通过将哈希函数作用于key上，我们得到了哈希值，基于哈希值我们可以去表里的相应位置获取对应的数据。除了存在哈希冲突问题之外，HashMap一个很大的问题就是空间效率低。引入Bloom Filter则可以很好的解决空间效率的问题。

原理
Bloom Filter是一种空间效率很高的随机数据结构，Bloom filter 可以看做是对bit-map 的扩展，布隆过滤器被设计为一个具有N的元素的位数组A（bit array），初始时所有的位都置为0。

当一个元素被加入集合时，通过K个Hash函数将这个元素映射成一个位阵列（Bit array）中的K个点，把它们置为1。检索时，我们只要看看这些点是不是都是1就（大约）知道集合中有没有它了。



如果这些点有任何一个 0，则被检索元素一定不在；
如果都是 1，则被检索元素很可能在。
添加元素
要添加一个元素，我们需要提供k个哈希函数。每个函数都能返回一个值，这个值必须能够作为位数组的索引（可以通过对数组长度进行取模得到）。然后，我们把位数组在这个索引处的值设为1。例如，第一个哈希函数作用于元素I上，返回x。类似的，第二个第三个哈希函数返回y与z，那么：
A[x]=A[y]=A[z] = 1

查找元素
查找的过程与上面的过程类似，元素将会被会被不同的哈希函数处理三次，每个哈希函数都返回一个作为位数组索引值的整数，然后我们检测位数组在x、y与z处的值是否为1。如果有一处不为1，那么就说明这个元素没有被添加到这个布隆过滤器中。如果都为1，就说明这个元素在布隆过滤器里面。当然，会有一定误判的概率。

算法优化
通过上面的解释我们可以知道，如果想设计出一个好的布隆过滤器，我们必须遵循以下准则：

好的哈希函数能够尽可能的返回宽范围的哈希值。
位数组的大小（用m表示）非常重要：如果太小，那么所有的位很快就都会被赋值为1，这样就增加了误判的几率。
哈希函数的个数（用k表示）对索引值的均匀分配也很重要。
计算m的公式如下：
m = - nlog p / (log2)^2
这里p为可接受的误判率。

计算k的公式如下：
k = m/n log(2)
这里k=哈希函数个数，m=位数组个数，n=待检测元素的个数（后面会用到这几个字母）。

哈希算法
哈希算法是影响布隆过滤器性能的地方。我们需要选择一个效率高但不耗时的哈希函数，在论文《更少的哈希函数，相同的性能指标：构造一个更好的布隆过滤器》中，讨论了如何选用2个哈希函数来模拟k个哈希函数。首先，我们需要计算两个哈希函数h1(x)与h2(x)。然后，我们可以用这两个哈希函数来模仿产生k个哈希函数的效果：
gi(x) = h1(x) + ih2(x)
这里i的取值范围是1到k的整数。

Google Guava类库使用这个技巧实现了一个布隆过滤器，哈希算法的主要逻辑如下：

long hash64 = ...;
int hash1 = (int) hash64;
int hash2 = (int) (hash64 >>> 32);

for (int i = 1; i <= numHashFunctions; i++) {
  int combinedHash = hash1 + (i * hash2);
  // Flip all the bits if it's negative (guaranteed positive number)
  if (combinedHash < 0) {
    combinedHash = ~combinedHash;
  }
}

一、基本概念：

        布隆过滤器（Bloom Filter）是由布隆（Burton Howard Bloom）在1970年提出的。它实际上是由一个很长的二进制向量（位向量）和一系列随机映射函数组成，布隆过滤器可以用于检索一个元素是否在一个集合中。它的优点是空间效率和查询时间都远远超过一般的算法，缺点是有一定的误识别率（假正例False positives，即Bloom Filter报告某一元素存在于某集合中，但是实际上该元素并不在集合中）和删除困难，但是没有识别错误的情形（即假反例False negatives，如果某个元素确实没有在该集合中，那么Bloom Filter 是不会报告该元素存在于集合中的，所以不会漏报）。因此，Bloom Filter不适合那些“零错误”的应用场合。而在能容忍低错误率的应用场合下，Bloom Filter通过极少的错误换取了存储空间的极大节省。

    如果想判断一个元素是不是在一个集合里，一般想到的是将所有元素保存起来，然后通过比较确定。链表，树等等数据结构都是这种思路. 但是随着集合中元素的增加，我们需要的存储空间越来越大，检索速度也越来越慢。不过世界上还有一种叫作散列表（又叫哈希表，Hash table）的数据结构。它可以通过一个Hash函数将一个元素映射成一个位阵列（Bit Array）中的一个点。这样一来，我们只要看看这个点是不是 1 就知道可以集合中有没有它了。这就是布隆过滤器的基本思想。

     Hash面临的问题就是冲突。假设 Hash 函数是随机的，如果我们的位阵列长度为 m 个点，那么如果我们想将冲突率降低到例如 1%, 这个散列表就只能容纳 m/100 个元素。显然这就不叫空间有效了（Space-efficient）。解决方法也简单，就是使用多个 Hash函数，如果它们有一个说元素不在集合中，那肯定就不在(必须对应位置上都是1)。如果它们都说在，有很小的可能性该元素不在。

BloomFilter的几个重要参数：

  插入集合的元素个数n，BloomFilter位数组的长度m，hash函数个数k

优点

    相比于其它的数据结构，布隆过滤器在空间和时间方面都有巨大的优势。布隆过滤器存储空间和插入/查询时间都是常数，取决于hash函数的个数k(O(k))。另外, Hash 函数相互之间没有关系，方便并行实现。布隆过滤器不需要存储元素本身，在某些对保密要求非常严格的场合有优势。

    布隆过滤器可以表示全集，其它任何数据结构都不能；

    k 和 m 相同，使用同一组 Hash 函数的两个布隆过滤器的交并差运算可以使用位操作进行。

缺点

    布隆过滤器的缺点和优点一样明显。误算率（False Positive）是其中之一。随着存入的元素数量增加，误算率随之增加。但是如果元素数量太少，则使用散列表足矣。

    另外，一般情况下不能从布隆过滤器中删除元素. 我们很容易想到把位列阵变成整数数组，每插入一个元素相应的计数器加1, 这样删除元素时将计数器减掉就可以了。然而要保证安全的删除元素并非如此简单。首先我们必须保证删除的元素的确在布隆过滤器里面. 这一点单凭这个过滤器是无法保证的。另外计数器回绕也会造成问题。

 

二、算法描述
    一个空的 bloom filter是一个有m bits的bit array，每一个bit位都初始化为0。并且定义有k个不同的hash函数，每个都随机将元素hash到m个不同位置中的一个。在下面的介绍中n为元素数，m为布隆过滤器或哈希表的位数，k为布隆过滤器hash函数个数。

    为了add一个元素，用k个hash函数将它hash得到bloom filter中k个bit位，将这k个bit位置1。

    为了query一个元素，即判断它是否在集合中，用k个hash function将它hash得到k个bit位。若这k bits全为1，则此元素在集合中；若其中任一位不为1，则此元素比不在集合中（因为如果在，则在add时已经把对应的k个bits位置为1）。

    不允许remove元素，因为那样的话会把相应的k个bits位置为0，而其中很有可能有其他元素对应的位。因此remove会引入false negative，这是绝对不被允许的。

    当k很大时，设计k个独立的hash function是不现实并且困难的。对于一个输出范围很大的hash function（例如MD5产生的128 bits数），如果不同bit位的相关性很小，则可把此输出分割为k份。或者可将k个不同的初始值（例如0,1,2, … ,k-1）结合元素，feed给一个hash function从而产生k个不同的数。

    当add的元素过多时，即n/m过大时（n是元素数，m是bloom filter的bits数），会导致false positive过高，此时就需要重新组建filter，但这种情况相对少见。

二. 时间和空间上的优势

当可以承受一些误报时，布隆过滤器比其它表示集合的数据结构有着很大的空间优势。例如self-balance BST, tries, hash table或者array, chain，它们中大多数至少都要存储元素本身，对于小整数需要少量的bits，对于字符串则需要任意多的bits（tries是个例外，因为对于有相同prefixes的元素可以共享存储空间）；而chain结构还需要为存储指针付出额外的代价。对于一个有1%误报率和一个最优k值的布隆过滤器来说，无论元素的类型及大小，每个元素只需要9.6 bits来存储。这个优点一部分继承自array的紧凑性，一部分来源于它的概率性。如果你认为1%的误报率太高，那么对每个元素每增加4.8 bits，我们就可将误报率降低为原来的1/10。add和query的时间复杂度都为O(k)，与集合中元素的多少无关，这是其他数据结构都不能完成的。

如果可能元素范围不是很大，并且大多数都在集合中，则使用确定性的bit array远远胜过使用布隆过滤器。因为bit array对于每个可能的元素空间上只需要1 bit，add和query的时间复杂度只有O(1)。注意到这样一个哈希表（bit array）只有在忽略collision并且只存储元素是否在其中的二进制信息时，才会获得空间和时间上的优势，而在此情况下，它就有效地称为了k=1的布隆过滤器。

而当考虑到collision时，对于有m个slot的bit array或者其他哈希表（即k=1的布隆过滤器），如果想要保证1%的误判率，则这个bit array只能存储m/100个元素，因而有大量的空间被浪费，同时也会使得空间复杂度急剧上升，这显然不是space efficient的。解决的方法很简单，使用k>1的布隆过滤器，即k个hash function将每个元素改为对应于k个bits，因为误判度会降低很多，并且如果参数k和m选取得好，一半的m可被置为为1，这充分说明了布隆过滤器的space efficient性。



https://blog.csdn.net/he_ranly/article/details/94433004


# 
https://www.toutiao.com/i6822106782794514957/
https://redis.io/topics/sentinel

http://pages.cs.wisc.edu/~cao/papers/summary-cache/node8.html

