package com.test.struct;

public class ThreadRun1 {

	

	private static class T1 extends Thread {
		
		public void run() {
			for(int i=0;i<10;i++) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(i);
			}
			
		}
	}
	
	public ThreadRun1() {
		//启动线程的三种方式：Thread，Runnable , Exceutors.newCacheThreadPool();
	}
	
	public void joinMain() {
		
		//|t2     t1 |
		// |    
		// | 
		// #t1.join()
		//        |
		//        |
		// |
		// |
		
		Thread t1 = new Thread(()->{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			for(int i=0;i<10;i++) {
				System.out.println("t1:"+i);
			}
		});
		
		Thread t2 = new Thread(()->{
			for(int i=0;i<10;i++) {
				System.out.println("t2:"+i);
			}
			try {
				t1.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(int i=10;i<20;i++) {
				System.out.println("t2:"+i);
			}
		});
		
		t2.start();
		t1.start();
		
		//不要去关闭线程，容易产生状态不一致
		// Forces the thread to stop executing.
		//t2.stop();
		
	}
	
	public void yieldMain() {
		//线程让步。使用了这个方法之后，线程会让出CPU执行权，让自己或者其它的线程运行。
		//也就是说，当前线程调用yield()之后，并不能保证：其它具有相同优先级的线程一定能获得执行权，
		//也有可能是当前线程又进入到“运行状态”继续运行。
		//返回到就绪状态
		new Thread(()->{
			for(int i=0;i<100;i++) {
				System.out.println("A"+i);
				if(i%10==0) Thread.yield();
			}
		}).start();
		
		new Thread(()->{
			for(int i=0;i<100;i++) {
				System.out.println("--B"+i);
				if(i%10==0) Thread.yield();
			}
		}).start();
	}
	
	
	public void main() {
		
		T1 t1 = new T1();
		t1.start();
		
		for(int i=0;i<10;i++) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("main:"+i);
		}
	}
}
