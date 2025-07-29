package com.itheima.test02;

public class test {
    public static void main(String[] args) {

        Thread chef = new Chef();
        Thread foodie = new Foodie();

        chef.start();
        foodie.start();

    }
}
