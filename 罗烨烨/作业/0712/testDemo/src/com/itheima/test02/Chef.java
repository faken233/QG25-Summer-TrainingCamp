package com.itheima.test02;

public class Chef extends Thread {

    @Override
    public void run() {
        while (true) {
            synchronized (Desk.objLock) {
                if (Desk.eatNumber == 0) {
                    Desk.objLock.notify();
                    break;
                }
                if (Desk.foodNumber == 0) {
                    System.out.println("厨师在干活");
                    Desk.foodNumber++;
                }
                if (Desk.foodNumber == 1) {
                    try {
                        Desk.objLock.notify();
                        Desk.objLock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}