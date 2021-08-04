package com.zhytnik.algo.brand.threshold;

import java.util.Objects;

import static java.lang.Math.abs;
import static java.lang.Math.max;

public interface Threshold {

    boolean isAcceptable(double actual, double expected);

    default Threshold and(Threshold other) {
        Objects.requireNonNull(other);
        return (actual, expected) -> isAcceptable(actual, expected) && other.isAcceptable(actual, expected);
    }

    static Threshold whichAcceptsRangeLike(double left, double right) {
        double diff = abs(left - right);
        return new AbsoluteErrorThreshold(diff).and(new RelativeErrorThreshold(diff / abs(max(left, right))));
    }
}
