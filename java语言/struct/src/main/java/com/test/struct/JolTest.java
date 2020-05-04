package com.test.struct;

import java.util.ArrayList;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;

public class JolTest {

	
	public void main() throws InterruptedException {
		
		  //偏向锁 延迟4秒启动 +UserBiasedStartupDelay=0 or  Thread.sleep(5000)
		  //markword 8byte +  class point 4byte + 成员变量
		  //默认启动类指针缩，不压缩8byte
		  //压缩
		  Thread.sleep(5000);
		  
		  Object o = new Object();
		  System.out.println(ClassLayout.parseClass(ArrayList.class).toPrintable());
		  System.out.println(GraphLayout.parseInstance(ArrayList.class).toPrintable());
		  System.out.println("----");
  
		  System.out.println(ClassLayout.parseInstance(o).toPrintable());
		  System.out.println(GraphLayout.parseInstance(o).toPrintable());
		  
		  synchronized(o){
			  System.out.println(ClassLayout.parseInstance(o).toPrintable());
		  }
	}
}
