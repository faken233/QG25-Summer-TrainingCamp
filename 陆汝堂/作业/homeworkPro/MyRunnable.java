package homeworkPro;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class MyRunnable implements Runnable{


    ArrayList<ArrayList<String>> list;

    static int ticket =0;

    CountDownLatch latch;

    public MyRunnable(ArrayList<ArrayList<String>> list, CountDownLatch latch) {
        this.list = list;

        this.latch = latch;
    }


    @Override
    public void run() {


        ArrayList<String> ticketList = new ArrayList<>();
        ticketList.add(Thread.currentThread().getName());
        try {
            while(true){
                synchronized (MyRunnable.class){

                    if (ticket >= 100){
                        break;
                    }else {
                        ticket++;
                        ticketList.add(ticket+"");
                        Thread.yield();
                        Thread.sleep(100);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            list.add(ticketList);
            latch.countDown();
        }

    }
}
