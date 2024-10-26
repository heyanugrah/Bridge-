package org.legacy;

public class Calculator {

    @Bridge("native_method")
    public void add(){
        System.out.println("hello");
    }

    @Bridge
    public void display(){};
}
