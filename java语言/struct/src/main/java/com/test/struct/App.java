package com.test.struct;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;

import com.test.struct.ParkLockTest.ParkLocker;

/**
 * Hello world!
 * java -Xmx20M -Dfile.encoding=UTF-8 com.test.struct.App
 */

public class App {

	public static int counter = 0;
	public static ReentrantLock lock = new ReentrantLock();
	public static Condition condition = lock.newCondition();
//
//	public static void threadNormal() {
//
//		class MyThread extends Thread {
//			public void run() {
//				System.out.println("run");
//			}
//		}
//
//		MyThread myThread1 = new MyThread();
//		myThread1.start();
//	}
//
//	public static void threadRunnable() {
//		class MyThread implements Runnable {
//
//			public void run() {
//				// TODO Auto-generated method stub
//				System.out.println("runable");
//			}
//		}
//		MyThread myThread = new MyThread();
//		Thread thread = new Thread(myThread);
//		thread.start();
//	}
//
//	

//	public static void semphoreThread() {
//		ExecutorService exec = Executors.newCachedThreadPool();
//		// 只能5个线程同时访问
//		final Semaphore semp = new Semaphore(5);
//		// 模拟20个客户端访问
//		for (int index = 0; index < 20; index++) {
//			final int NO = index;
//			Runnable run = new Runnable() {
//				public void run() {
//					try {
//						// 获取许可
//						semp.acquire();
//						System.out.println("Accessing: " + NO);
//						// 模拟实际业务逻辑
//						// Thread.sleep((long) (Math.random() * 10000));
//						Thread.sleep(1000);
//						// 访问完后，释放
//						semp.release();
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//			};
//			exec.execute(run);
//		}
//
//		System.out.println(semp.getQueueLength());
//		// 退出线程池
//		exec.shutdown();
//	}


	public static void main(String[] args) throws InterruptedException {

		//GC调优程序1
//		JvmGc1 gc1 = new JvmGc1();
//		gc1.main();
		
		//JvmGc2.main();
		
		//Queue
		PriorityQueueTest priorityQueueTest = new PriorityQueueTest();
		//priorityQueueTest.main(null);
		priorityQueueTest.mainDelayTest();	
		
		
		//强软弱虚引用
		AllReference reference = new AllReference();
//		reference.softReference();
//		reference.weakReference();
//		reference.PhantomReference();
//		reference.threadLocalWeekRefer();

		// 多线程
		ThreadRun1 tr = new ThreadRun1();
		// tr.main();
		// tr.yieldMain();
		// tr.joinMain();
		ThreadStatus ts = new ThreadStatus();
		// ts.main();

		VolatileTest vt = new VolatileTest();
		// vt.main();

		ParkLockTest parklocker = new ParkLockTest();
		//parklocker.main();
		

		
		//ReentanceLock 
		MyBlockingQueue<Integer> blockingQueue = new MyBlockingQueue<Integer>(5);
//		new Thread(()->{
//			for(Integer i=0;i<10;i++) {
//				try {
//					blockingQueue.enqueue(i);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}).start();
//		
//		new Thread(()->{
//			for(Integer i=0;i<20;i++) {
//				try {
//					System.out.println(blockingQueue.dequeue());
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}).start();
		
		
		//TransferQueue交替打印
		TransferQueueTest transferQueueTest =new  TransferQueueTest();
//		transferQueueTest.main();
//		
//		//ThreadPool
		ThreadPoolTest threadPoolTest = new ThreadPoolTest();
//		threadPoolTest.main();
//		
		
		//阻塞等待固定线程数量执行完毕，-1操作直到0
		CountDownThread countDownThread =  new CountDownThread();
		//countDownThread.main();
		
		//FutureTaskTest
		FutureTaskTest futureTaskTest = new FutureTaskTest();
		//futureTaskTest.main();
		
		CyclicBarrierTest cyclicBarrierTest = new CyclicBarrierTest();
		//cyclicBarrierTest.main();
		
		
		StructTest structTest = new StructTest();
		//structTest.main();
		
		
		
		
//        System.out.println( "Hello World!" );

//        threadNormal();
//        threadRunnable();
//        threadFuture();
//        threadReentrenceLock();
//        semphoreThread();
		// countDown();
		// CountDownThread();
//        SephmoreTest semphore = new SephmoreTest();
//        semphore.doJob(10);
//        
//        
//        //https://www.jianshu.com/p/ae25eb3cfb5d
//        // 什么是CAS机制
//        // CAS是英文单词Compare And Swap的缩写，翻译过来就是比较并替换。
//        
//
//        class MyThread extends Thread {
//        	@Override
//        	public void run() {
//        		ReentrantLock lock = new ReentrantLock();
//        		try {
//        			if(lock.tryLock()) {
//                		counter++;
//                		
//            		}
//        		} finally {
//        			lock.unlock();
//        		}
//        		
//        		//synchronized(App.class) {
//        		//}
//        		// System.out.println(Thread.currentThread().getName() + " is running!");
//        	}
//        }
//        
//        ExecutorService pool = Executors.newCachedThreadPool();
//        Thread t1 = new MyThread();
//        Thread t2 = new MyThread();
//        Thread t3 = new MyThread();
//       
//        
//        pool.execute(t1);
//        pool.execute(t2);
//        pool.execute(t3);
//        
//        for(int i=0;i<1000;i++) {
//        	Thread t = new MyThread();
//        	pool.execute(t);
//        }
//        pool.shutdown();
//        
//        System.out.printf("%d \n", counter);
//        
//        
//        
//        lock.lock();
//        new Thread(new ThreadCondition()).start();
//        System.out.println("主线程等待通知");
//        try {
//            condition.await();
//        } finally {
//            lock.unlock();
//        }
//        System.out.println("主线程恢复运行");
//        
	}

	static class ThreadCondition implements Runnable {

		public void run() {
			// TODO Auto-generated method stub
			try {
				lock.lockInterruptibly();
				condition.signal();
				System.out.println("子线程通知");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}
		// https://www.cnblogs.com/takumicx/p/9338983.html
		// http://www.sohu.com/a/216162666_132276
		// https://www.jianshu.com/p/76959115d486
	}
}
