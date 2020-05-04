package com.test.struct;

import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ThreadPoolTest {

	
	
	public void main() throws InterruptedException {

		
		//ThreadPool
//		  int corePoolSize,
//        int maximumPoolSize,
//        long keepAliveTime,
//        TimeUnit unit,
//        BlockingQueue<Runnable> workQueue,
//        RejectedExecutionHandler handler
		
//		ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出RejectedExecutionException异常。
//		ThreadPoolExecutor.DiscardPolicy：也是丢弃任务，但是不抛出异常。
//		ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程）
//		ThreadPoolExecutor.CallerRunsPolicy：由调用线程处理该任务
		
		
		//问题是 maximumPoolSize Integer.MAX_VALUE 可能造成内存溢出
		//BlockingQueue 如果没有设置大小，可以造成oom
//		return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
//                60L, TimeUnit.SECONDS,
//                new SynchronousQueue<Runnable>());

		Executors.newCachedThreadPool();
		Executors.newFixedThreadPool(10);
		
		
		//周期性的执行
		Executors.newSingleThreadExecutor();
		ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
		service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                long start = new Date().getTime();
                System.out.println("scheduleAtFixedRate 开始执行时间:" +
                        DateFormat.getTimeInstance().format(new Date()));
            }
        },1000,2000,TimeUnit.MILLISECONDS);
		Thread.sleep(5000);
		service.shutdown();
	
		//提交自定义进程池
		class MyThread implements Runnable {

			public void run() {
				// TODO Auto-generated method stub
				System.out.println("runable");
			}
		}
		MyThread myThread = new MyThread();
		for(int i=0;i<10;i++) {
			ThreadPoolService.newTask(myThread);
		}
		
	}
	
}
