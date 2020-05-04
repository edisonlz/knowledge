package com.test.struct;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingQueue<E> {
	
	
	private int size = 10;
	private ReentrantLock lock = new ReentrantLock();
	LinkedList<E> list = new LinkedList<E>();
	
	Condition empty = lock.newCondition();
	Condition full = lock.newCondition();
	
	
	public BlockingQueue(int size) {
		this.size = size;
	}
	
	public void enqueue(E data) {
		
		lock.lock();
		try {
			if (this.size == list.size()) {
				 full.await();
			}
			
			list.add(data);
			System.out.println("入队"+data);
			empty.signal();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			lock.unlock();
		}
	}
	
	public E dequeue() {
		
		lock.lock();
		try {
			if(list.size()==0) {
				empty.await();
			}
			E e = list.removeFirst();
			System.out.println("出队："+e);
			full.signal();
			return e;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		
		return null;
		
	}
	
	
	
}
