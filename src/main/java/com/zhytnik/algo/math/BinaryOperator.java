package com.zhytnik.algo.math;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.BiFunction;
import java.util.function.DoubleBinaryOperator;

@AllArgsConstructor
public enum BinaryOperator implements Operator {

    ADDITION(
            '+',
            true,
            Double::sum,
            (x, y) -> x + "+" + y
    ),
    DIVISION(
            '/',
            false,
            (x, y) -> x / y,
            (x, y) -> x + "/" + y
    ),
    SUBTRACTION(
            '-',
            false,
            (x, y) -> x - y,
            (x, y) -> x + "-" + y
    ),
    MULTIPLICATION(
            '*',
            true,
            (x, y) -> x * y,
            (x, y) -> x + "*" + y
    );

    @Getter
    private final char symbol;
    @Getter
    private final boolean commutative;
    private final DoubleBinaryOperator calculation;
    private final BiFunction<String, String, String> writing;

    public double apply(Expression left, Expression right) {
        return calculation.applyAsDouble(left.value(), right.value());
    }

    public String writing(Expression left, Expression right) {
        return writing.apply(format(left), format(right));
    }

    private String format(Expression expression) {
        return expression.isUnary() ? expression.writtenFormat() : "(" + expression.writtenFormat() + ")";
    }
}
