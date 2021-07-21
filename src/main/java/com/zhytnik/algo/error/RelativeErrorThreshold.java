package com.zhytnik.algo.error;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class RelativeErrorThreshold implements BiDoublePredicate {

    private final double relativeError;

    @Override
    public boolean test(double actual, double expected) {
        return Math.abs((actual - expected) / expected) <= relativeError;
    }
}
