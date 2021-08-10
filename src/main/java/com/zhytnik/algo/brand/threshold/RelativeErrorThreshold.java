package com.zhytnik.algo.brand.threshold;

public final class RelativeErrorThreshold implements Threshold {

    private final double relativeError;

    public RelativeErrorThreshold(double relativeError) {
        if (relativeError < 0) {
            throw new IllegalArgumentException("Relative error should not be negative! Actual is " + relativeError);
        }
        this.relativeError = relativeError;
    }

    @Override
    public boolean isAcceptable(double actual, double expected) {
        return Math.abs((actual - expected) / expected) <= relativeError;
    }
}
