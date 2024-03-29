package com.zhytnik.algo.brand.data;

import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public final class BinaryOperation implements Expression {

    private final double value;

    @Getter
    private final Expression left;

    @Getter
    private final Expression right;

    @Getter
    private final BinaryOperator operator;

    private int hash;

    public BinaryOperation(Expression left, Expression right, BinaryOperator operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
        this.value = operator.apply(left, right);
    }

    @Override
    public double value() {
        return value;
    }

    @Override
    public boolean isUnary() {
        return false;
    }

    @Override
    public List<Variable> variables() {
        return Stream.concat(
                left.variables().stream(),
                right.variables().stream()
        ).collect(toList());
    }

    @Override
    public String formatted() {
        return operator.writing(left, right);
    }

    @Override
    public Expression recalculateWith(Map<? extends Expression, ? extends Expression> replacements) {
        var modifiedLeft = recalculate(left, replacements);
        var modifiedRight = recalculate(right, replacements);

        if (modifiedLeft == left && modifiedRight == right) {
            return this;
        }
        return new BinaryOperation(modifiedLeft, modifiedRight, operator);
    }

    private Expression recalculate(Expression source, Map<? extends Expression, ? extends Expression> replacements) {
        var r = replacements.get(source);
        return (r != null) ? r : source.recalculateWith(replacements);
    }

    @Override
    public String toString() {
        return formatted();
    }

    @Override
    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        if (another == null || getClass() != another.getClass()) {
            return false;
        }

        var op = (BinaryOperation) another;

        return operator.equals(op.operator) && left.equals(op.left) && right.equals(op.right);
    }

    @Override
    public int hashCode() {
        int h = hash;
        if (h == 0) {
            hash = h = (31 * left.hashCode() + right.hashCode()) ^ operator.hashCode();
        }
        return h;
    }
}
