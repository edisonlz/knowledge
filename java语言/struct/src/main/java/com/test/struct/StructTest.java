package com.test.struct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class StructTest {

	public void main() throws InterruptedException {

      /** ArrayList **/
      ArrayList<String> a = new ArrayList<String>();
      
      a.add("a");
      a.add("b");
      
      for(int i=0;i<a.size();i++) {
      	System.out.println(a.get(i));
      }
      
      for(String s : a) {
      	System.out.println(s);
      }
      
      /** LinkedList **/
      LinkedList<String> ll =  new LinkedList<String>();
      ll.add("l1");
      ll.add("l2");
      
      for(String e:ll) {
      	System.out.println(e);
      }
      /* Vector synchronized 线程安全 */
      Vector<Integer> v = new Vector<Integer>();
      v.add(1);
      v.add(2);
      
      for(Integer e : v) {
      	System.out.println(e);
      }
      
      
      /* HashSet */
      HashSet<String> set = new HashSet<String>();
      set.add("s1");
      set.add("s2");
      set.add("s1");
      set.add("s2");
      set.add("s3");
      
      for(String e:set) {
      	System.out.println(e);
      }
      
      /*  LinkedHashSet 保证有序 */
      LinkedHashSet<String> lset = new LinkedHashSet<String>();
      
      lset.add("s1");
      lset.add("s1");
      lset.add("s2");
      lset.add("s3");
      lset.add("s1");
      for(String e:lset) {
      	System.out.println(e);
      }
     
      /* 红黑树 二叉树自平衡 */
      TreeSet<String> tset = new TreeSet<String>();
      tset.add("t1");
      tset.add("t2");
      tset.add("t1");
      tset.add("t3");
      
      
      for(String e:tset) {
      	System.out.println(e);
      }
      ArrayBlockingQueue<String> aQueue =new  ArrayBlockingQueue<String>(10);
      aQueue.put("q1");
      System.out.println(aQueue.take());
      System.out.println(aQueue.poll(3,TimeUnit.SECONDS));
      
      
      /* Map */
      HashMap<String,String> hmap = new HashMap<String,String>();
      hmap.put("key1", "v1");
      hmap.put("key2", "v2");
      hmap.put("key3", "v3");
      hmap.put("key5", "v5");
      for(String key:hmap.keySet()){
      	System.out.println(key);
      	System.out.println(hmap.get(key));
      }
      
      TreeMap<String,String> tmap = new TreeMap<String,String>();
      tmap.put("kt1", "v1");
      tmap.put("kt2", "v2");
      tmap.put("kt3", "v3");
      tmap.put("kt5", "v5");
      tmap.put("kt6", "v6");
      for(String key : tmap.keySet()) {
      	System.out.println(key);
      	System.out.println(tmap.get(key));
      }
      
      /* LinkedHashMap  */
      LinkedHashMap<String,String>  lmap = new LinkedHashMap<String,String>();
      lmap.put("kt1", "v1");
      lmap.put("kt2", "v2");
      lmap.put("kt3", "v3");
      lmap.put("kt5", "v5");
      lmap.put("kt6", "v6");
      for(String key : lmap.keySet()) {
      	System.out.println(key);
      	System.out.println(lmap.get(key));
      }
      

      ConcurrentHashMap<String,String> cmap = new ConcurrentHashMap<String,String>();
      cmap.put("c1", "v1");
      cmap.put("c2","v2");
      for(String key:cmap.keySet()) {
      	System.out.println(key);
      }
      
      Hashtable<String,String>  htmap = new Hashtable<String,String>();
      htmap.put("key1", "v1");

	}
}
