package com.es2.factorymethod;

public class Main {
    public static void main(String[] args) {
        try {
            Product computer = FactoryProduct.makeProduct("Computer");
            computer.setBrand("Dell");

            Product software = FactoryProduct.makeProduct("Software");
            software.setBrand("Microsoft");

            System.out.println("Computer brand: " + computer.getBrand());
            System.out.println("Software brand: " + software.getBrand());
        } catch (UndefinedProductException exception) {
            System.out.println("Erro ao criar produto.");
        }
    }
}