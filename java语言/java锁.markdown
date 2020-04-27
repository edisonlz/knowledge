#### 第一部分 锁

#### 1.CAS 
![multi](./imgs/cpu.png)

```java
//Compare and Exchange/swap
//该指令不原子性，所以使用北桥总线锁lock，解决并发冲突
//为什么不是原子性
//1.比较，2.替换
//在第二步替换的情况可能产生冲突
// [外设] -南桥- cpu -北桥- [内存]
//ams汇编
lock comxchg new old
```

####2.Synchronize
###### 对象头(64位)
![multi](./imgs/64JOL.png)
###### 对象头相关材料
对象头包含两部分：Mark Word 和 Class Metadata Address


```java
markword.hpp
https://www.ixigua.com/pseries/6804774509673972228_6803950860578587148/
```


###### JOL Java Object Layout
```java
//偏向锁 延迟4秒启动 +UserBiasedStartupDelay=0 or  Thread.sleep(5000)
//延迟4秒启动在于，启动时一般会有多线程处理，如果多线程处理下偏向锁会让程序变慢，变慢原因为可能会产生频繁撤销偏向锁+偏向锁。
//markword 8byte +  class point 4byte + 成员变量
//默认启动类指针缩，不压缩8byte
//压缩
Thread.sleep(5000);
Object o = new Object();
System.out.println(ClassLayout.parseClass(ArrayList.class).toPrintable());
System.out.println(GraphLayout.parseInstance(ArrayList.class).toPrintable());
System.out.println("----");
System.out.println(ClassLayout.parseInstance(o).toPrintable());
System.out.println(GraphLayout.parseInstance(o).toPrintable());

synchronized(o){
    //加速后打印对象头
    System.out.println(ClassLayout.parseInstance(o).toPrintable());
}
```

###### Synchronized 锁升级过程
```java
2用户态 + 1内核态，优化上下文切换
偏向锁 - 自适应自旋锁 - 重量级锁(放入等待队列，优化cpu消耗) Mutex/Futex
```
###### 1.偏向锁
 - 通过CAS锁更改对象头
 - 进行锁撤销，如果撤销失败，升级为自旋锁/轻量级锁

###### 2.自适应自旋锁
- 锁竞争加剧，自旋消耗cpu ，升级为重量级锁，进入等待队列
- CAS Thread [LOCK Record] -  JObjectHeader

###### 3.重量级锁
```java
//https://www.cnblogs.com/deltadeblog/p/9559035.html
//https://www.cnblogs.com/longshiyVip/p/5213771.html
当多个线程同时请求某个对象监视器时，对象监视器会设置几种状态用来区分请求的线程：
Contention List：所有请求锁的线程将被首先放置到该竞争队列
Entry List：Contention List中那些有资格成为候选人的线程被移到Entry List
Wait Set：那些调用wait方法被阻塞的线程被放置到Wait Set
OnDeck：任何时刻最多只能有一个线程正在竞争锁，该线程称为OnDeck
Owner：获得锁的线程称为Owner
!Owner：释放锁的线程
```


#### 3.volatile
```java
1. 保证可见性
一个cache line 64byte

[cache line] [cache line] l2      [cache line]  [cache line] l2
         [cache line] l1      [cache line] l1 
            [cpu core1]                 [cpu core2]
int A = 1
cpu core 1 - Thread1 - A = 2
cpu core 2 - Thread1 - A = 1[不可见]
如何保证可见性
1.CPU[intelx86] MESI 一致性缓存算法 Modify，Exclusive,Share, Invalid
2.lock锁总线，l1,l2,l3全部更新完释放
```

```java
2. 防止指令重排

JVM 增加屏障
JSR内存屏障
LoadLoad屏障: load1;LoadLoad;load2;
StoreStore屏障：store1;StoreStore;store2;
LoadStore屏障：load1;LoadLoad;store2;
StoreLoad屏障：store1;LoadLoad;load2;


内核级别
sfence,lfence,mfence
```

##### volatile 如何解决指令重排总结：
1. volatile i
2. ACC_VOLATILE
3. JVM的内存屏障
   屏障两边的指令不可以重排！
4. hotspot实现
bytecodeinterpreter.cpp


###### 指令重排容易出现的问题，半初始化状态，异常出现
```java
new  申请对象内存，m = 0
init m = 8
astore s指向m(引用)， s->m 
```
如果发生指令重排，同时另一个线程获取到初始化半个的对象，s=0,就会出现问题。
```
new  申请对象内存，m = 0
astore s指向m(引用)， s->m 
init m = 8
```


```java
public class SemiInitTest {

    public int s = 8;
	private static volatile SemiInitTest sit = null;
	
	public SemiInitTest() {
		//pass
	}
	
	public static SemiInitTest getInstance() {
		//DCL Double check Lock
		if(sit == null) {
			synchronized(sit) {
				if(sit == null) {
					sit = new SemiInitTest();
				}
			}
		}
		return sit;
	}
}
```
 
#### 4.Reentrencelock
##### 知识点
- AQS 
- LockSupport.park()/unpark()
- CAS


```java
//CAS + LockSupport + 非安全队列方便理解
//实现简单的锁（用户态锁）道格.里为了解决老版本synchronized重量级锁而设计的
public class ParkLockTest {

	public class ParkLocker {
	
		private long state = 0;
		private LinkedList<Thread> queue = new LinkedList<Thread>();//队列底层实现
	    
	    @SuppressWarnings("restriction")
		public Unsafe getUnsafe() {
			try {
				Field field = Unsafe.class.getDeclaredField("theUnsafe");
				field.setAccessible(true);
				return (Unsafe)field.get(null);
				
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			return null;
	    }
		
		@SuppressWarnings("restriction")
		public void lock() {
			Unsafe unsafe  = getUnsafe();
			long stateOffset = 0;
			try {
				stateOffset = unsafe.objectFieldOffset(ParkLocker.class.getDeclaredField("state"));
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
			if(getUnsafe().compareAndSwapInt(this, stateOffset, 0, 1)) {
				//cas竞争成功, 什么也不干，不阻塞
				System.out.println("lock acquired");
				return;
			} else {
				System.out.println("lock blocked");
				Thread t = Thread.currentThread();
				queue.add(t);
				LockSupport.park(t);
			}
		}
		
		public void unlock() {
			
			if(state>0) {
				state -= 1;
				if(!queue.isEmpty()) {
					Thread t = queue.removeFirst();
					LockSupport.unpark(t);
					//引入重新竞争
				}
			}
			
		}

	}
	
	public void main() {
		ParkLocker locker = new ParkLocker();
		Thread t1 = new Thread(()->{
			locker.lock();
			System.out.println("t1");
			locker.unlock();
		});
		
		Thread t2 = new Thread(()->{
			locker.lock();
			System.out.println("t2");
			locker.unlock();
		});
		
		t1.start();
		t2.start();
	}
	
}
```



```AQS学习
https://www.bilibili.com/video/BV19J411Q7R5?from=search&seid=17440062686262339576
```

##### 公平非公平锁
- 公平锁-排队AQS
- 先抢占(cas)在排队


##### 使用 ReentrantLock + Condition实现阻塞队列
```java
public class MyBlockingQueue<E> {

    int size;//阻塞队列最大容量

    ReentrantLock lock = new ReentrantLock();
    LinkedList<E> list=new LinkedList<E>();//队列底层实现

    Condition full = lock.newCondition();//队列满时的等待条件
    Condition empty = lock.newCondition();//队列空时的等待条件

    public MyBlockingQueue(int size) {
        this.size = size;
    }

    public void enqueue(E e) throws InterruptedException {
        lock.lock();
        try {
            while (list.size() ==size)//队列已满,在notFull条件上等待
                full.await();
            
            list.add(e);//入队:加入链表末尾
            System.out.println("入队：" +e);
            empty.signal(); //通知在notEmpty条件上等待的线程
        } finally {
            lock.unlock();
        }
    }

    public E dequeue() throws InterruptedException {
        E e;
        lock.lock();
        try {
            while (list.size() == 0)//队列为空,在notEmpty条件上等待
            	empty.await();
            
            e = list.removeFirst();//出队:移除链表首元素
            System.out.println("出队："+e);
            full.signal();//通知在notFull条件上等待的线程
            return e;
        } finally {
            lock.unlock();
        }
    }
}
```

```java
//ReentanceLock 
		MyBlockingQueue<Integer> blockingQueue = new MyBlockingQueue<Integer>(5);
		
		new Thread(()->{
			for(Integer i=0;i<10;i++) {
				try {
					blockingQueue.enqueue(i);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		
		new Thread(()->{
			for(Integer i=10;i<20;i++) {
				try {
					blockingQueue.enqueue(i);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		
		new Thread(()->{
			for(Integer i=0;i<20;i++) {
				try {
					System.out.println(blockingQueue.dequeue());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
```

#### 5.锁自适应

##### 锁消除 lockeliminate
```java
// 我们都知道StringBuffer是线程安全的，
// 我们发现，sb这个引用只会在add方法中使用，不可能被其他线程使用，
// 因此，sb是不可能共享的资源，JVM会自动消除StringBuffer对象内部的锁。
public void(String str1,String str2) {
    StringBuffer sb = new StringBuffer();
    sb.append(str1).append(str2);
}
```

##### 锁粗化lock coarsening
```java
public String test(String str){
    int i = 0;
    StringBuffer sb = new StringBuffer();
    while(i<100){
        sb.append(str);
        i++;
    }
    return sb.toString();
}
```



#### 相关材料
https://zhuanlan.zhihu.com/p/113624654
https://www.ixigua.com/pseries/6804774509673972228_6801021393203888644/
https://www.ixigua.com/pseries/6804774509673972228_6801021393203888644/
https://www.cnblogs.com/z00377750/p/9180644.html
https://www.jianshu.com/p/0e036fa7af2a

