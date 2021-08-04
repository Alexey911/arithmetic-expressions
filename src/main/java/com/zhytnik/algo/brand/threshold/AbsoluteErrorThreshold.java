package com.zhytnik.algo.brand.threshold;

public final class AbsoluteErrorThreshold implements Threshold {

    private final double absoluteError;

    public AbsoluteErrorThreshold(double absoluteError) {
        this.absoluteError = Math.abs(absoluteError);
    }

    @Override
    public boolean isAcceptable(double actual, double expected) {
        return Math.abs(actual - expected) <= absoluteError;
    }
}
