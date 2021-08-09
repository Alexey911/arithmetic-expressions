package com.zhytnik.algo.brand.filter;

import com.zhytnik.algo.brand.compute.Transformation;
import com.zhytnik.algo.brand.data.BinaryOperation;
import com.zhytnik.algo.brand.data.Expression;
import com.zhytnik.algo.brand.data.Variable;
import com.zhytnik.algo.brand.threshold.Threshold;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonMap;

@AllArgsConstructor
public final class VariableElimination implements Transformation {

    private static final Variable ONE = new Variable("erased-value", 1.0);
    private static final Variable ZERO = new Variable("erased-value", 0.0);

    private final int complexity;
    private final Threshold equal;

    @Override
    public long complexity(int outputSize) {
        return Math.multiplyFull(complexity, outputSize);
    }

    @Override
    public List<Expression> apply(List<Expression> source) {
        List<Expression> significant = new ArrayList<>();

        Counter invalid = new Counter();

        for (var expression : source) {
            double original = expression.value();

            for (var variable : expression.variables()) {
                traverse(expression, expression, variable, original, invalid);

                if (invalid.isNonZero()) {
                    break;
                }
            }

            if (invalid.isNonZero()) {
                invalid.reset();
            } else {
                significant.add(expression);
            }
        }
        return significant;
    }

    private boolean traverse(Expression expression, Expression source,
                             Variable target, double original, Counter invalid) {
        if (!(expression instanceof BinaryOperation)) {
            return false;
        }

        var op = (BinaryOperation) expression;

        boolean found = tryToEliminate(target, op, original, source, invalid);

        if (!found) {
            found = traverse(op.getLeft(), source, target, original, invalid);
        }
        if (!found) {
            found = traverse(op.getRight(), source, target, original, invalid);
        }
        return found;
    }

    private boolean tryToEliminate(Variable target, BinaryOperation op, double original,
                                   Expression source, Counter invalid) {
        if (op.getLeft() == target) {
            switch (op.getOperator()) {
                case MULTIPLICATION:
                    tryToEliminate(target, ONE, original, source, invalid);
                    break;
                case ADDITION:
                case SUBTRACTION:
                case DIVISION:
                    tryToEliminate(target, ZERO, original, source, invalid);
            }
            return true;
        } else if (op.getRight() == target) {
            switch (op.getOperator()) {
                case DIVISION:
                case MULTIPLICATION:
                    tryToEliminate(target, ONE, original, source, invalid);
                    break;
                case ADDITION:
                case SUBTRACTION:
                    tryToEliminate(target, ZERO, original, source, invalid);
            }
            return true;
        }
        return false;
    }

    private void tryToEliminate(Variable elimination, Variable replacement, double original, Expression source, Counter invalid) {
        if (equal.isAcceptable(replacement.value(), elimination.value())) {
            return;
        }

        var modified = source.recalculateWith(singletonMap(elimination, replacement));

        if (equal.isAcceptable(modified.value(), original)) {
            invalid.increment();
        }
    }
}
