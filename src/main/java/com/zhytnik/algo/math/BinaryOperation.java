package com.zhytnik.algo.math;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class BinaryOperation implements Expression {

    private final double value;

    @Getter
    private final Expression left;

    @Getter
    private final Expression right;

    @Getter
    private final BinaryOperator operator;

    private int hash = 0;

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
    public List<Variable> variables() {
        var lefts = left.variables();
        var rights = right.variables();

        List<Variable> target = new ArrayList<>(lefts.size() + rights.size());

        target.addAll(lefts);
        target.addAll(rights);
        return target;
    }

    @Override
    public String writtenFormat() {
        return operator.writing(left, right);
    }

    @Override
    public boolean isUnary() {
        return false;
    }

    @Override
    public String toString() {
        return writtenFormat();
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
            hash = h = 31 * (left.hashCode() ^ right.hashCode()) + operator.hashCode();
        }
        return h;
    }
}
