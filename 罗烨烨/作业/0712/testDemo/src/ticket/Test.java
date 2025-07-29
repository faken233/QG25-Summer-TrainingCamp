package ticket;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;


public class Test {
    public static void main(String[] args) throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(60);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(60);

        System.out.println("即将开始抢票...\n============================================");

        for (int i = 1; i <= 60; i++) {
            new Thread(new RushTicket(cyclicBarrier, countDownLatch, "游客" + i + "号")).start();
        }

        System.out.println("抢票开始\n========================================================");

        // 主线程等待所有线程完成
        countDownLatch.await();

        System.out.println("\n所有抢票线程已完成！");
    }
}
