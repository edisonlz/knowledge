package com.test.struct;

import java.io.IOException;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;

public class AllReference {
	
	private static final ReferenceQueue<M> QUEUE = new  ReferenceQueue<M>();
	private static final List<Object> LIST = new  LinkedList<Object>();
	
	public class M {
	
		@Override
		protected void finalize() throws Throwable {
			System.out.println("finalize");
		}
	}
	public AllReference() {
		//pass
		//强软弱虚引用
	}
	
	public void normalReference() throws IOException {
		M m = new M();
		m = null;
		System.gc();
		System.in.read();
	}
	
	public void softReference()  {
		SoftReference<byte[]> m = new SoftReference<byte[]>(new byte[1024*1024*10]);
		
		System.out.println(m.get());
		System.gc();
		
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(m.get());
		
		byte[] b = new byte[1024*1024*10];
		System.out.println(m.get());
	}
	
	public void weakReference() {
		WeakReference<M> m = new WeakReference<M>(new M());
		System.out.println(m.get());
		System.gc();
		System.out.println(m.get());
		
	}
	
	
	public void PhantomReference() {
		//NIO 管理堆外内存
		// DirectByteBuffer --- c++[堆外内存] 
		//        |
		//Queue [ | | |  ] 
		
		PhantomReference<M> phanthomReference = new PhantomReference<M>(new M(), QUEUE);
		new Thread(()-> {
			LIST.add(new byte[1024 * 1024]);
			while(true) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(phanthomReference.get());
			}
			
		}).start();
		
		new Thread(()-> {
			while(true) {
				Reference<? extends M> poll = QUEUE.poll();
				if(poll!=null) {
					System.out.println("jvm 虚引用对象---"+poll);
				}
			}
		}).start();
	}
	
	public void threadLocalWeekRefer() {

		//        ThreadLocalMap
		// Key   Value
		// key1      value
		//   |
		// WeakReference
		//   |
		// ThreadLocal
		//为什么key是弱引用，防止内存泄漏
		//如果threadlocal 被回收依然有内存泄漏，value
		
		ThreadLocal<M> tl = new ThreadLocal<M>();
		new Thread(()-> 
		{
			try {
				Thread.sleep(2000);
				//TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(tl.get());
		}).start();
		
		new Thread(()-> 
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tl.set(new M());
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tl.remove();//防止内存泄漏
		}).start();
		
	}
}
