package com.zhytnik.algo.brand.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.BiFunction;

@AllArgsConstructor
public enum BinaryOperator {

    ADDITION(
            "+",
            true,
            (x, y) -> x + "+" + y
    ) {
        @Override
        public double apply(Expression left, Expression right) {
            return left.value() + right.value();
        }
    },
    DIVISION(
            "/",
            false,
            (x, y) -> x + "/" + y
    ) {
        @Override
        public double apply(Expression left, Expression right) {
            return left.value() / right.value();
        }
    },
    SUBTRACTION(
            "-",
            false,
            (x, y) -> x + "-" + y
    ) {
        @Override
        public double apply(Expression left, Expression right) {
            return left.value() - right.value();
        }
    },
    MULTIPLICATION(
            "*",
            true,
            (x, y) -> x + "*" + y
    ) {
        @Override
        public double apply(Expression left, Expression right) {
            return left.value() * right.value();
        }
    };

    @Getter
    private final String display;
    @Getter
    private final boolean commutative;
    private final BiFunction<String, String, String> writing;

    public abstract double apply(Expression left, Expression right);

    String writing(Expression left, Expression right) {
        return writing.apply(format(left), format(right));
    }

    private String format(Expression expression) {
        return expression.isUnary() ? expression.formatted() : "(" + expression.formatted() + ")";
    }
}
