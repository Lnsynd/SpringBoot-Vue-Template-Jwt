package com.example;

/**
 * Created by 刘千山 on 2023/9/7/007-15:03
 */
public class Singleton2 {
    // 懒汉模式
    public static volatile Singleton2 instance = null;
    private Singleton2(){

    }

    public static Singleton2 getInstance(){
        if(instance == null){
            synchronized (Singleton2.class){
                if(instance==null){
                    instance = new Singleton2();
                }
            }

        }
        return instance;
    }

    public static void main(String[] args) {
        Singleton2 instance1 = Singleton2.getInstance();
    }
}
