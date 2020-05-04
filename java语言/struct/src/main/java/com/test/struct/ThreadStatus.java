package com.test.struct;

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
		
		
		//主状态干： new ruannable ternimated
		//3个阻塞状态：waiting ,TIMED_WAITING, Block
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
