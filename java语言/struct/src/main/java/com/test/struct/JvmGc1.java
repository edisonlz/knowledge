package com.test.struct;

import java.util.LinkedList;
import java.util.List;

public class JvmGc1 {

	public void main() {
		System.out.println("Hello GC");
		List list = new LinkedList();
		
		for(;;) {
			byte[] b = new byte[1024*1024];
			list.add(b);
		}
	}
}
