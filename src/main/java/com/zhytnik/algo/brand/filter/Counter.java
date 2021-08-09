package com.zhytnik.algo.brand.filter;

public final class Counter {

    private int value;

    public int value() {
        return value;
    }

    public void increment() {
        value++;
    }

    public boolean isNonZero() {
        return value == 0;
    }

    public void reset() {
        value = 0;
    }
}
