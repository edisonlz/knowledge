package com.test.struct;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class FutureTaskTest {

	
	public  void main() throws InterruptedException {
		ExecutorService executor = Executors.newCachedThreadPool();
		ArrayList<FutureTask<String>> tasks = new ArrayList<FutureTask<String>>();

		class CallableDemo implements Callable<String> {
			public String call() throws Exception {
				// 假设随机的运行时间
				Random r = new Random();
				int i = r.nextInt(100);
				TimeUnit.SECONDS.sleep(1);
				return String.valueOf(i);
			}
		}

		for (int i = 0; i < 15; i++) {
			CallableDemo callableDemo = new CallableDemo();
			FutureTask<String> futureTask = new FutureTask(callableDemo);
			executor.submit(futureTask);
			tasks.add(futureTask);
		}

		//TimeUnit.SECONDS.sleep(5);
		for (FutureTask<String> task : tasks) {
			try {
				String s = task.get(3, TimeUnit.SECONDS);
				System.out.println(s);
			} catch (Exception e) {
				System.err.println("e = " + e.getMessage());
			}
		}
	}
}
