package com.test.struct;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.LockSupport;

import sun.misc.Unsafe;

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
		    
			if(unsafe.compareAndSwapInt(this, stateOffset, 0, 1)) {
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
