package com.zhytnik.algo.error;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class AbsoluteErrorThreshold implements BiDoublePredicate {

    private final double absoluteError;

    @Override
    public boolean test(double actual, double expected) {
        return Math.abs(actual - expected) <= absoluteError;
    }
}
