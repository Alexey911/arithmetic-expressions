package com.zhytnik.algo.math;

import lombok.AllArgsConstructor;

import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;

@AllArgsConstructor
public enum UnaryOperator implements Operator {

    EQUAL(
            value -> value,
            variable -> variable
    ),
    NEGATE(
            value -> -value,
            variable -> "-" + variable
    ),
    MOD(
            Math::abs,
            variable -> ("|" + variable + "|")
    );

    private final DoubleUnaryOperator calculation;
    private final Function<String, String> writing;

    public double apply(Expression expression) {
        return calculation.applyAsDouble(expression.value());
    }

    public String writing(Expression expression) {
        return writing.apply(format(expression));
    }

    private String format(Expression expression) {
        return expression.isUnary() ? expression.writtenFormat() : "(" + expression.writtenFormat() + ")";
    }
}
