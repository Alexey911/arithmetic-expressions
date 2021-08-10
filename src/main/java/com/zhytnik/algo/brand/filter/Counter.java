package com.zhytnik.algo.brand.filter;

final class Counter {

    private int value;

    public int value() {
        return value;
    }

    public void increment() {
        value++;
    }

    public boolean isZero() {
        return value == 0;
    }

    public void reset() {
        value = 0;
    }
}
