package org.example;

public class MyClass {
//    static {
//        System.loadLibrary("mylib"); // Load the native library
//    }

    public native void myNativeMethod();

    public static void main(String[] args) {
        new MyClass().myNativeMethod();
    }
}
