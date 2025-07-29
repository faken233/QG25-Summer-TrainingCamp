package com.itheima.test01;

public class test {
    public static void main(String[] args) {
        MyThread myThread1 = new MyThread();
        MyThread myThread2 = new MyThread();

        myThread1.setName("myThread1");
        myThread2.setName("myThread2");

        myThread1.start();
        myThread2.start();

    }
}
