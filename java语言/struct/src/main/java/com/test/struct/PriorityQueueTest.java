package com.test.struct;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.PriorityQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class PriorityQueueTest {

	public void main(String[] args) {
		// Create a Priority Queue

		PriorityQueue<Integer> numbers = new PriorityQueue<>();

		// Add items to a Priority Queue (ENQUEUE)
		numbers.add(3);
		numbers.add(1);
		numbers.add(2);

		// Remove items from the Priority Queue (DEQUEUE)
		while (!numbers.isEmpty()) {
			System.out.println(numbers.remove());
		}
	}

	class Item implements Delayed {
	    /* 触发时间*/
	    private long excuteTime;
	    String name;
	 
	    public Item(String name, long time, TimeUnit unit) {
	        this.name = name;
	        //this.time = System.currentTimeMillis() + (time > 0? unit.toMillis(time): 0);
	        this.excuteTime = TimeUnit.NANOSECONDS.convert(time, TimeUnit.MILLISECONDS) + System.nanoTime();
	    }
	 
	    @Override
	    public long getDelay(TimeUnit unit) {
	    	//System.out.println(System.currentTimeMillis());
	        //return time - System.currentTimeMillis();
	        return unit.convert(this.excuteTime - System.nanoTime(), TimeUnit.NANOSECONDS);
	    }
	 
	    @Override
	    public int compareTo(Delayed o) {
	        Item item = (Item) o;
	        long diff = this.excuteTime - item.excuteTime;
	        if (diff <= 0) {// 改成>=会造成问题
	            return -1;
	        }else {
	            return 1;
	        }
	    }
	 
	    @Override
	    public String toString() {
	        return "Item{" +
	                "time=" + excuteTime +
	                ", name='" + name + '\'' +
	                '}';
	    }
	}
	
	
	public void mainDelay() throws InterruptedException {
		DelayQueue<Item>  delayQueue = new DelayQueue<Item>();
	    Item item1 = new Item("item1", 1, TimeUnit.SECONDS);
        Item item2 = new Item("item2", 5, TimeUnit.SECONDS);
        Item item3 = new Item("item3", 10, TimeUnit.SECONDS);
        
        DelayQueue<Item> queue = new DelayQueue<Item>();
        queue.put(item1);
        queue.put(item2);
        queue.put(item3);
        
        for (int i = 0; i < 3; i++) {
            Item take = queue.take();
            System.out.format("name:{%s}, time:{%s}\n",take.name, LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        }
	}
	
	public void mainDelayTest() throws InterruptedException {
		
		System.out.println(System.nanoTime());
		Item item1 = new Item("item1",1000,TimeUnit.SECONDS);
		Item item2 = new Item("item2",5000,TimeUnit.SECONDS);
		Item item3 = new Item("item3",10000,TimeUnit.SECONDS);
		DelayQueue<Item> delayQueue = new DelayQueue<Item>();
		delayQueue.add(item1);
		delayQueue.add(item2);
		delayQueue.add(item3);
		
		 for (int i = 0; i < 3; i++) {
			 Item item = delayQueue.take();
			 System.out.println(item);
		 }
		
	}
	
}
