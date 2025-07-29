package homeworkPro;

import java.util.ArrayList;
import java.util.concurrent.*;

public class demo {
    public static void main(String[] args) throws InterruptedException {
        ArrayList<ArrayList<String>> list = new ArrayList<>();

        ThreadFactory nameThreadFactory = new ThreadFactory() {
            int count = 1;

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("抢票人-" + count++);
                return thread;
            }
        };

        CountDownLatch countDownLatch = new CountDownLatch(10);

        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                100,
                100,
                60,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(100),
                nameThreadFactory,
                new ThreadPoolExecutor.AbortPolicy()
        );



        for (int i = 0; i < 100; i++) {
            pool.submit(new MyRunnable(list, countDownLatch));
        }


        countDownLatch.await();


        for (int i = 0; i < list.size(); i++) {
            ArrayList<String> ticketList  = list.get(i);
            String threadName = ticketList.get(0);
            if (ticketList.size()<=1){
                System.out.println(threadName + "没抢到票");
            }else {
                System.out.printf(threadName + "抢到了第 ");
                for (int j = 1; j < ticketList.size(); j++) {
                    System.out.print(ticketList.get(j) + " ");
                }
                System.out.println("张票，共" + (ticketList.size() - 1) + "张");
            }
        }

        pool.shutdown();
    }
}
