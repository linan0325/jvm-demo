package com.nan.jvm.serviceJvm;

import com.nan.jvm.obj.A;
import com.nan.jvm.obj.B;
import org.springframework.stereotype.Service;

@Service
public class Demo1 {
     static A a = new A();
     static B b = new B();
    public void cpuTest(){
        System.out.println("jvm test");
        new Thread(null,()->{

            while (true){

            }

        },"thread 1").start();

        new Thread(null,()->{

            try{
                Thread.sleep(1000000l);
            }catch (Exception e){
                e.printStackTrace();
            }

        },"thread 2").start();
    }
    
    
    public void LockThread() throws InterruptedException{

        new Thread(()->{

            synchronized (a){
                try {
                    Thread.sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }

                synchronized (b){
                    System.out.println("获得a和b");
                }
            }



        }).start();

        Thread.sleep(1000);

        new Thread(()->{

            synchronized (b){
                synchronized (a){
                    System.out.println("获得a和b");
                }
            }

        }).start();
    }
}
