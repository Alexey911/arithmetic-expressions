package com.zhytnik.algo.error;

import lombok.AllArgsConstructor;

import java.util.function.BiPredicate;

@AllArgsConstructor
public class RelativeErrorThreshold implements BiPredicate<Double, Double> {

    private final double relativeError;

    @Override
    public boolean test(Double actual, Double expected) {
        return Math.abs((actual - expected) / expected) <= relativeError;
    }
}
