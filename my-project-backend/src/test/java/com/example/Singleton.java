package com.example;

/**
 * Created by 刘千山 on 2023/9/7/007-14:39
 */
public class Singleton {
    // 饿汉模式 初始化的时候直接创建出实例
    public static Singleton instance1 = new Singleton();
    private Singleton(){

    }
    public static Singleton getInstance() {
        return instance1;
    }

    public static void main(String[] args) {
        Singleton singleton = Singleton.getInstance();
    }

}
