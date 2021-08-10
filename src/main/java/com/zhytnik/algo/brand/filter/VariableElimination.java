package com.zhytnik.algo.brand.filter;

import com.zhytnik.algo.brand.compute.Transformation;
import com.zhytnik.algo.brand.data.BinaryOperation;
import com.zhytnik.algo.brand.data.Expression;
import com.zhytnik.algo.brand.data.Variable;
import com.zhytnik.algo.brand.threshold.AbsoluteErrorThreshold;
import com.zhytnik.algo.brand.threshold.Threshold;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class VariableElimination implements Transformation {

    private static final double DEFAULT_VALUE_PRECISION = 0.000001d;
    private static final Variable ONE = new Variable("erased-value", 1.0d);
    private static final Variable ZERO = new Variable("erased-value", 0.0d);

    private final int complexity;
    private final Threshold valueThreshold;
    private final Threshold targetThreshold;

    //TODO(Zhytnik): add precision customization
    public VariableElimination(int complexity, Threshold targetThreshold) {
        this(complexity, targetThreshold, new AbsoluteErrorThreshold(DEFAULT_VALUE_PRECISION));
    }

    public VariableElimination(int complexity, Threshold targetThreshold, Threshold valueThreshold) {
        if (complexity <= 0) {
            throw new IllegalArgumentException("Complexity should be positive! Actual is " + complexity);
        }
        this.complexity = complexity;
        this.valueThreshold = Objects.requireNonNull(valueThreshold);
        this.targetThreshold = Objects.requireNonNull(targetThreshold);
    }

    @Override
    public long complexity(int sourceSize) {
        return Math.multiplyFull(complexity, sourceSize);
    }

    @Override
    public List<Expression> apply(List<Expression> source) {
        var invalid = new Counter();
        var valuable = new ArrayList<Expression>();

        for (var expression : source) {
            double original = expression.value();

            for (var variable : expression.variables()) {
                traverse(expression, variable, expression, invalid, original);

                if (invalid.isNonZero()) {
                    break;
                }
            }

            if (invalid.isNonZero()) {
                invalid.reset();
            } else {
                valuable.add(expression);
            }
        }
        return valuable;
    }

    private boolean traverse(Expression expression, Variable target,
                             Expression source, Counter invalid, double original) {
        if (!(expression instanceof BinaryOperation)) {
            return false;
        }

        var op = (BinaryOperation) expression;

        boolean found = tryToEliminate(op, target, source, invalid, original);

        if (!found) {
            found = traverse(op.getLeft(), target, source, invalid, original);
        }
        if (!found) {
            found = traverse(op.getRight(), target, source, invalid, original);
        }
        return found;
    }

    private boolean tryToEliminate(BinaryOperation op, Variable target,
                                   Expression source, Counter invalid, double original) {
        if (op.getLeft() == target) {
            switch (op.getOperator()) {
                case MULTIPLICATION:
                    tryToEliminate(target, ONE, invalid, source, original);
                case ADDITION:
                case SUBTRACTION:
                case DIVISION:
                    tryToEliminate(target, ZERO, invalid, source, original);
            }
            return true;
        } else if (op.getRight() == target) {
            switch (op.getOperator()) {
                case DIVISION:
                    tryToEliminate(target, ONE, invalid, source, original);
                    break;
                case MULTIPLICATION:
                    tryToEliminate(target, ONE, invalid, source, original);
                case ADDITION:
                case SUBTRACTION:
                    tryToEliminate(target, ZERO, invalid, source, original);
            }
            return true;
        }
        return false;
    }

    private void tryToEliminate(Variable elimination, Variable replacement,
                                Counter invalid, Expression source, double original) {
        if (valueThreshold.isAcceptable(replacement.value(), elimination.value())) {
            return;
        }

        var modified = source.recalculateWith(Map.of(elimination, replacement));

        if (targetThreshold.isAcceptable(modified.value(), original)) {
            invalid.increment();
        }
    }
}
