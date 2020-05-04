package com.test.struct;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountDownThread {

	public void main() {

		final CountDownLatch latch = new CountDownLatch(2);
		ExecutorService pool = Executors.newCachedThreadPool();
		
		pool.execute(new Runnable() {
			public void run() {
				try {
					Thread.sleep(2000);
					System.out.println("执行:" + Thread.currentThread().getName());
					latch.countDown();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		pool.execute(new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(1000);
					System.out.println("执行:" + Thread.currentThread().getName());
					latch.countDown();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});


		try {
			latch.await();
			pool.shutdown();
			System.out.println("执行完成");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
