package com.test.struct;

import java.util.concurrent.Semaphore;

public class SephmoreTest {

	
	public void doJob(final int count) {
		
		
		final Semaphore sa = new Semaphore(1);
		final Semaphore sb = new Semaphore(0);
		final Semaphore sc = new Semaphore(0);
		
		new Thread(new Runnable() {

			public void run() {
				for(int i=0;i<count;i++) {
					// TODO Auto-generated method stub
					try {
						sa.acquire();
						System.out.println("0");
						sb.release();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
		
		new Thread(new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				for(int i=0;i<count;i++) {
					try {
						sb.acquire();
						System.out.println("1");
						sc.release();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
		
		new Thread(new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				for(int i=0;i<count;i++) {
					try {
						sc.acquire();
						System.out.println("2");
						sa.release();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();

	}
}
