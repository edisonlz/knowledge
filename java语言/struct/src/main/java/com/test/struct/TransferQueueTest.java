package com.test.struct;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

public class TransferQueueTest {

	
	public void main() {
		char[] char1 = "12345".toCharArray();
		char[] char2 = "ABCDE".toCharArray();
		
		TransferQueue<Character> queue = new LinkedTransferQueue<Character>();
		
		new Thread(()->{
		
			for(char c : char1){
				try {
					queue.transfer(c);
					System.out.println(queue.take());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		
		new Thread(()->{
			for(char c : char2){
				try {
					System.out.println(queue.take());
					queue.transfer(c);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		
		
	}
}
