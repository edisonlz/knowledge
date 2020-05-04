package com.test.struct;

public class SemiInitTest {

	private static volatile SemiInitTest sit = null;
	
	public SemiInitTest() {
		//pass
	}
	
	public static SemiInitTest getInstance() {
		//DCL Double check Lock
		if(sit == null) {
			synchronized(sit) {
				if(sit == null) {
					sit = new SemiInitTest();
				}
			}
		}
		return sit;
	}
}
