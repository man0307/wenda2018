package com.batman;

import java.util.concurrent.*;

public class MultiThreadTest {
    public static void testBlockigQueue(){
        BlockingQueue<String> blockingQueue=new ArrayBlockingQueue<String>(3);
        new Thread(new Customer(blockingQueue),"customer").start();
        new Thread(new Producer(blockingQueue),"proceduer").start();

    }
public static void main(String[] args){
       testFuture();
}
public static void testExecutor(){
    ExecutorService executor= Executors.newFixedThreadPool(2);
    //shutdown是指执行完了再结束
}

//future异步处理请求结果 阻塞等待
public static void testFuture(){
    ExecutorService service=Executors.newSingleThreadExecutor();
    Future<Integer> future= service.submit(new Callable<Integer>() {
        @Override
        public Integer call() throws Exception {

            Thread.sleep(3000);

            return 1;

        }
    });

    service.shutdown();
    try {
        System.out.println(future.get(100,TimeUnit.MILLISECONDS));
    } catch (InterruptedException e) {
        e.printStackTrace();
    } catch (ExecutionException e) {
        e.printStackTrace();
    } catch (TimeoutException e) {
        e.printStackTrace();
    }
}
}

class Customer implements Runnable {
    BlockingQueue<String> blockingQueue;

    public Customer(BlockingQueue<String> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }
    @Override
    public void run() {
        for(int i=0;i<100;i++){
            try {
                System.out.println(Thread.currentThread().getName()+":"+blockingQueue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

class Producer implements Runnable {

    BlockingQueue<String> blockingQueue;

    public Producer(BlockingQueue<String> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {

            try {
                for (int i = 0; i < 100; i++) {
                    Thread.sleep(1000);
                    blockingQueue.put(String.valueOf(i));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

}