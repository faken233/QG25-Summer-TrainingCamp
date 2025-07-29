package ticket;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class RushTicket implements Runnable {
    private final CyclicBarrier cyclicBarrier;
    private final CountDownLatch countDownLatch;
    private final String name;
    private int ticketId = 0;
    private boolean ifSuccess = false;

    public RushTicket(CyclicBarrier cyclicBarrier, CountDownLatch countDownLatch, String name) {
        this.cyclicBarrier = cyclicBarrier;
        this.countDownLatch = countDownLatch;
        this.name = name;
    }

    @Override
    public void run() {
        try {
            // 等待所有线程准备就绪
            cyclicBarrier.await();
            while (true) {
                if (SellTicket.ticketNum == 0 || ifSuccess) {
                    break;
                }
                SellTicket.fairLock.lock();
                try {
                    if (SellTicket.ticketNum > 0) {
                        ticketId = SellTicket.ALL_TICKET_NUM - --SellTicket.ticketNum;
                        ifSuccess = true;
                    }
                } finally {
                    SellTicket.fairLock.unlock();
                }
            }
            if (ifSuccess) {
                System.out.println("用户：" + name + "，抢到了第" + ticketId + "张票");
            } else {
                System.out.println("-->用户：" + name + "没有抢到票！！");
            }
        } catch (InterruptedException | BrokenBarrierException e) {
            Thread.currentThread().interrupt();
            System.out.println(e.getMessage());
        } finally {
            countDownLatch.countDown();
        }
    }
}