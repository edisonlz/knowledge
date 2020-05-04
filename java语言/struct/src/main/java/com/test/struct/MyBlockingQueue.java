package com.test.struct;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MyBlockingQueue<E> {

	int size = 0;
	ReentrantLock lock = new ReentrantLock();
	LinkedList<E> queue = new LinkedList<E>();
	Condition empty = lock.newCondition();
	Condition full = lock.newCondition();
	
	public MyBlockingQueue(int size) {
		this.size = size;
	}
	
	public void enqueue(E e) throws InterruptedException {
		
		lock.lock();
		try {
			if(queue.size() == size) {
				full.await();
			}
			queue.add(e);
			empty.signal();
		} finally {
			lock.unlock();
		}
	}
	
	
	public E dequeue() throws InterruptedException {
		lock.lock();
		try {
			if(queue.isEmpty()) {
				empty.await();
			}
			E e = queue.removeFirst();
			full.signal();
			return e;
		} finally {
			lock.unlock();
		}
	}

}