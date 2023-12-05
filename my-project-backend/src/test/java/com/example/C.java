package com.example;

/**
 * Created by 刘千山 on 2023/9/19/019-20:47
 */
class C {
    C(){
        System.out.println("C");
    }
}
class A{
    C c = new C();
    A(){
        this("A");
        System.out.println("A");
    }
    A(String s){
        System.out.println(s);
    }
}

class Test1 extends A{
    Test1(){
        super("B");
        System.out.println("B");
    }

    public static void main(String[] args) {
        new Test1();
    }
}
