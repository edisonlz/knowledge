package com.test.struct;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class DelayQueueTest {

	static class DelayTask implements Delayed {
		private static long currentTime = System.currentTimeMillis();
		protected final String taskName;
		protected final int timeCost;
		protected final long scheduleTime;

		protected static final AtomicInteger taskCount = new AtomicInteger(0);

		// 定时任务之间的启动时间间隔在1~2s之间，timeCost表示处理此任务需要的时间，本示例中为2s
		public DelayTask(String taskName, int timeCost) {
			this.taskName = taskName;
			this.timeCost = timeCost;
			taskCount.incrementAndGet();
			currentTime += 1000 + (long) (Math.random() * 1000);
			scheduleTime = currentTime;
		}

		@Override
		public int compareTo(Delayed o) {
			return (int) (this.scheduleTime - ((DelayTask) o).scheduleTime);
		}

		@Override
		public long getDelay(TimeUnit unit) {
			long expirationTime = scheduleTime - System.currentTimeMillis();
			return unit.convert(expirationTime, TimeUnit.MILLISECONDS);
		}

		public void execTask() {
			long startTime = System.currentTimeMillis();
			System.out.println("Task " + taskName + ": schedule_start_time=" + scheduleTime + ",real start time="
					+ startTime + ",delay=" + (startTime - scheduleTime));
			try {
				Thread.sleep(timeCost);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	class DelayTaskComsumer extends Thread {
		private final BlockingQueue<DelayTask> queue;

		public DelayTaskComsumer(BlockingQueue<DelayTask> queue) {
			this.queue = queue;
		}

		@Override
		public void run() {
			DelayTask task = null;
			try {
				while (true) {
					task = queue.take();
					task.execTask();
					DelayTask.taskCount.decrementAndGet();
				}
			} catch (InterruptedException e) {
				System.out.println(getName() + " finished");
			}
		}
	}

	public void main() {
		BlockingQueue<DelayTask> queue = new DelayQueue<DelayTask>();

		for (int i = 0; i < 10; i++) {
			try {
				queue.put(new DelayTask("work " + i, 2000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		ThreadGroup g = new ThreadGroup("Consumers");

		for (int i = 0; i < 1; i++) {
			new Thread(g, new DelayTaskComsumer(queue)).start();
		}

		while (DelayTask.taskCount.get() > 0) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		g.interrupt();
		System.out.println("Main thread finished");

	}

}
