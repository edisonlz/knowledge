
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
