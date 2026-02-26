package com.es2.factorymethod;

public class Software implements Product {
    private String brand;

    protected Software() {
    }

    @Override
    public String getBrand() {
        return brand;
    }

    @Override
    public void setBrand(String brand) {
        this.brand = brand;
    }
}