package com.itheima.test02;

public class Foodie extends Thread {

    @Override
    public void run() {
        while (true) {
            synchronized (Desk.objLock) {
                if (Desk.eatNumber == 0) {
                    Desk.objLock.notify();
                    break;
                }
                if (Desk.foodNumber == 1) {
                    Desk.eatNumber--;
                    System.out.println("桌子上有食物，开吃，还能吃" + Desk.eatNumber + "碗");
                    Desk.foodNumber--;
                }
                if (Desk.foodNumber == 0) {
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
