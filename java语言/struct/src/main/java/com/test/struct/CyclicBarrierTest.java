package com.test.struct;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierTest {

	public void main() {
		CyclicBarrier cyclicBarrier = new CyclicBarrier(10, () -> {
			System.out.println("会议可以开始了...");
		});

		for (int i = 1; i <= 10; i++) {
			new Thread(() -> {
				System.out.println("参会人员" + Thread.currentThread().getName() + "到达会议室...");
				try {
					cyclicBarrier.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (BrokenBarrierException e) {
					e.printStackTrace();
				}
			}, String.valueOf(i)).start();
		}
	}
	
	public void mainTo() {
		CyclicBarrier cyclicBarrier = new CyclicBarrier(10, ()->{
			System.out.println("所有人都进入了会议");
		});
		
		for(int i=1;i<=10;i++) {
			final int j = i;
			new Thread(()-> {
				System.out.println(j);
				try {
					cyclicBarrier.await();
				} catch (InterruptedException | BrokenBarrierException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}).start();
		}
			
	}

}
