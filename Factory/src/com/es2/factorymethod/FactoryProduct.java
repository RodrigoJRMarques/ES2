package com.es2.factorymethod;

public abstract class FactoryProduct {
    public FactoryProduct() {
    }

    public static Product makeProduct(String type) throws UndefinedProductException {
        if (type == null) {
            throw new UndefinedProductException();
        }

        String normalizedType = type.trim();
        if ("Computer".equalsIgnoreCase(normalizedType)) {
            return new Computer();
        }
        if ("Software".equalsIgnoreCase(normalizedType)) {
            return new Software();
        }

        throw new UndefinedProductException();
    }
}