package com.zhytnik.algo.error;

import java.util.Objects;

public interface BiDoublePredicate {

    boolean test(double left, double right);

    default BiDoublePredicate and(BiDoublePredicate other) {
        Objects.requireNonNull(other);
        return (left, right) -> test(left, right) && other.test(left, right);
    }
}
