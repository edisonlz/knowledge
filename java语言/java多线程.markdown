
##### 多线程状态：
![multi](./imgs/thread-status.png)

```java
//thread status

public class ThreadStatus {

	
	public ThreadStatus() {
		// Thread States
		//                      线程调度器来执行
		//                     | <- 挂起   |
	 	// New -> start() ->  ready <-> running  - > ternimated -》 o
		//                     |           |
		//                     |  <- yield |
		//  Thread.sleep(time)
		//  o.wait(time)
		//  t.join(time)                   |
		//  LockSupport.parkNanos() -> TimedWaiting
		//   LockSupport.parkUntil()
		// ----------------------------
		// os.wait();
		// t.join();
		// LockSupport.park() -> waiting -> o.notify(); o.notifyAll();LockSupport.unpark();
		// 同步锁                     Block
		
		//主状态干： New, Runnable, Ternimated
		//3个阻塞状态：Waiting ,TIMED_WAITING, Block
	}
	
	public void main() throws InterruptedException {
		Thread t1 = new Thread(()-> {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		System.out.println(t1.getState());
		t1.start();
		System.out.println(t1.getState());
		Thread.sleep(500);
		System.out.println(t1.getState());
		Thread.sleep(3000);
		System.out.println(t1.getState());
	}
	
}
```


##### 对象引用 
###### 强软弱虚
- NormalReference
- SoftReference 内存不足才会被回收
- WeakReference 只要System.gc()就回收
- PhantomReference 管理堆外内存


##### ThreadLocal 
```java
会不会内存泄漏，WeakReference  + tl.remove()

tl           - >                         ThreadLocal
                                            |   [weakreference]
thread.theadlocals - threadLocalMap-Entry[key,value] ---- [10M]
tl.remove() //删除value引用，不删除会出现内存泄漏,10M内存泄漏
t1 = null //删除key 引用和当前线程变量引用
```


##### 学习材料
https://www.ixigua.com/i6819557280590070286/
https://www.bilibili.com/video/av11076511/
https://www.bilibili.com/video/BV1RT4y137Qd
https://zhuanlan.zhihu.com/p/126384164
https://www.ixigua.com/i6807658279146095115/
https://zhidao.baidu.com/question/72768560.html
https://my.oschina.net/u/658658/blog/636007



# 线程池
https://www.cnblogs.com/dolphin0520/p/3932921.html


# 阿里自定义线程
https://www.cnblogs.com/xhq1024/p/12125290.html

# 线程池--拒绝策略RejectedExecutionHandler
https://www.jianshu.com/p/aa420c7df275
1.线程池状态

##### ThreadPoolExecutor runState

在ThreadPoolExecutor中定义了一个volatile变量，另外定义了几个static final变量表示线程池的各个状态：
volatile int runState;
static final int RUNNING    = 0;
static final int SHUTDOWN   = 1;
static final int STOP       = 2;
static final int TERMINATED = 3;

runState表示当前线程池的状态，它是一个volatile变量用来保证线程之间的可见性；
　　下面的几个static final变量表示runState可能的几个取值。
　　当创建线程池后，初始时，线程池处于RUNNING状态；
　　如果调用了shutdown()方法，则线程池处于SHUTDOWN状态，此时线程池不能够接受新的任务，它会等待所有任务执行完毕；
　　如果调用了shutdownNow()方法，则线程池处于STOP状态，此时线程池不能接受新的任务，并且会去尝试终止正在执行的任务；
　　当线程池处于SHUTDOWN或STOP状态，并且所有工作线程已经销毁，任务缓存队列已经清空或执行结束后，线程池被设置为TERMINATED状态。



####  CyclicBarrier 循环屏障
、　CyclicBarrier是一个同步辅助类，主要作用是让一组线程互相等待，知道都到达一个公共障点，在一起走。在涉及一组固定大小的线程的程序中，这些线程必须不时地互相等待，此时 CyclicBarrier 很有用。因为该 barrier 在释放等待线程后可以重用，所以称它为循环 的 barrier。

　　CyclicBarrier 支持一个可选的 Runnable 命令，在一组线程中的最后一个线程到达之后（但在释放所有线程之前），该命令只在每个屏障点运行一次。若在继续所有参与线程之前更新共享状态，此屏障操作 很有用



#### DelayQueue 
1.简介：
DelayQueue是一个无界阻塞队列，只有在延迟期满时，才能从中提取元素。
队列的头部，是延迟期满后保存时间最长的delay元素。
2.使用场景：
缓存系统设计：使用DelayQueue保存缓存元素的有效期，用一个线程循环查询DelayQueue，一旦从DelayQueue中取出元素，就表示有元素到期。
定时任务调度：使用DelayQueue保存当天要执行的任务和执行的时间，一旦从DelayQueue中获取到任务，就开始执行，比如Timer，就是基于DelayQueue实现的。
https://www.jianshu.com/p/6c3d5c7a386d

