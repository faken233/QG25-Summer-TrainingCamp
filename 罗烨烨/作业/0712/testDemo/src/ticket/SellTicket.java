package ticket;

import java.util.concurrent.locks.ReentrantLock;

public class SellTicket {

    public static final int ALL_TICKET_NUM = 50;
    public static int ticketNum = ALL_TICKET_NUM;
    public static ReentrantLock fairLock = new ReentrantLock(true);     // 公平锁，默认为false


}
