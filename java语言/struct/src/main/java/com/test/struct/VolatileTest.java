package com.test.struct;

public class VolatileTest {
	
	private  volatile boolean running = true;  
	
	public VolatileTest() {
		//volatile 可见性，MESI一致性算法 LOCK总线。防止指令重排序，增加屏障
	}
	
	public void m() {
		System.out.println("start m");
		while(running) {
			//
		}
		System.out.println("end m");
	}
	
	public void main() {
		Thread t = new Thread(()->{
			m();
		});
		t.start();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.running = false;
	}

}
