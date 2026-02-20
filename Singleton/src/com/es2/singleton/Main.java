package com.es2.singleton;

public class Main {
    public static void main(String[] args) {
        // Get the instance and set a value
        Registry.getInstance().setPath("/my/custom/path");

        // Prove it's a singleton by getting the instance again
        System.out.println("Path is: " + Registry.getInstance().getPath());
    }
}