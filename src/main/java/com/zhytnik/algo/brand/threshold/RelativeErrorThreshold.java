package com.zhytnik.algo.brand.threshold;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class RelativeErrorThreshold implements Threshold {

    private final double relativeError;

    @Override
    public boolean isAcceptable(double actual, double expected) {
        return Math.abs((actual - expected) / expected) <= relativeError;
    }
}
