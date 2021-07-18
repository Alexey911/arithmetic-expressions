package com.zhytnik.algo.error;

import lombok.AllArgsConstructor;

import java.util.function.BiPredicate;

@AllArgsConstructor
public class AbsoluteErrorThreshold implements BiPredicate<Double, Double> {

    private final double absoluteError;

    @Override
    public boolean test(Double actual, Double expected) {
        return Math.abs(actual - expected) <= absoluteError;
    }
}
