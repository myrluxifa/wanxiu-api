package com.wanxiu.api.runable;

import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class LootHongBao implements Callable {

    private String hongBaoKey;


    private ConcurrentHashMap<String, Vector<Integer>> m;



    public LootHongBao(String hongBaoKey,ConcurrentHashMap<String, Vector<Integer>> m) {
        // TODO Auto-generated constructor stub
        this.hongBaoKey=hongBaoKey;
        this.m=m;
    }


//    @Override
//    public void run() {
//        // TODO Auto-generated method stub
//        synchronized(m) {
//            Vector<Integer> v=m.get(hongBaoKey);
//            int s=v.size();
//            if((s-1)<0) {
//                System.out.println(Thread.currentThread().getName() + "抢光了！");
//            }else {
//                int money=v.get(s-1);
//                v.remove(s-1);
//                System.out.println(Thread.currentThread().getName() + "抢到 " + money + "秀币");
//            }
//        }
//
//    }

    @Override
    public Object call() throws Exception {
        int money;
        synchronized(m) {
            Vector<Integer> v=m.get(hongBaoKey);
            int s=v.size();
            if((s-1)<0) {
                System.out.println(Thread.currentThread().getName() + "抢光了！");
                money=0;
            }else {
                money=v.get(s-1);
                v.remove(s-1);
                System.out.println(Thread.currentThread().getName() + "抢到 " + money + "秀币");
            }
        }
        return money;
    }
}
