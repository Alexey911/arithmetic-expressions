package com.zhytnik.algo.transform;

import com.zhytnik.algo.error.BiDoublePredicate;
import com.zhytnik.algo.math.BinaryOperation;
import com.zhytnik.algo.math.Expression;
import com.zhytnik.algo.math.Variable;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

// change value and check result
@AllArgsConstructor
public class VariableElimination implements Transformation {

    private static final Variable ONE = new Variable("erased-value", 1.0);
    private static final Variable ZERO = new Variable("erased-value", 0.0);

    private final BiDoublePredicate equal;

    @Override
    public long expectedCount(int size) {
        return size;
    }

    @Override
    public List<Expression> apply(List<Expression> source) {
        List<Expression> significant = new ArrayList<>();

        Counter invalid = new Counter();

        for (var expression : source) {
            double original = expression.value();

            for (var variable : expression.variables()) {
                traverse(expression, Function.identity(), variable, original, invalid);

                if (invalid.count > 0) {
                    break;
                }
            }

            if (invalid.count > 0) {
                invalid.count = 0;
            } else {
                significant.add(expression);
            }
        }
        System.out.println("Eliminated: " + (source.size() - significant.size()));
        return significant;
    }

    private boolean traverse(Expression expression, Function<Expression, Expression> update,
                             Variable target, double original, Counter invalid) {
        if (!(expression instanceof BinaryOperation)) {
            return false;
        }

        var op = (BinaryOperation) expression;

        boolean found = tryToEliminate(target, op, original, update, invalid);

        if (!found) {
            found = traverse(
                    op.getLeft(),
                    modified -> update.apply(new BinaryOperation(modified, op.getRight(), op.getOperator())),
                    target, original, invalid
            );
        }
        if (!found) {
            found = traverse(
                    op.getRight(),
                    modified -> update.apply(new BinaryOperation(op.getLeft(), modified, op.getOperator())),
                    target, original, invalid
            );
        }
        return found;
    }

    private boolean tryToEliminate(Variable target, BinaryOperation source, double original,
                                   Function<Expression, Expression> update, Counter invalid) {
        if (source.getLeft() == target) {
            switch (source.getOperator()) {
                case MULTIPLICATION:
                    tryToEliminate(target, ONE, true, source, original, update, invalid);
                    break;
                case ADDITION:
                case SUBTRACTION:
                case DIVISION:
                    tryToEliminate(target, ZERO, true, source, original, update, invalid);
            }
            return true;
        } else if (source.getRight() == target) {
            switch (source.getOperator()) {
                case DIVISION:
                case MULTIPLICATION:
                    tryToEliminate(target, ONE, false, source, original, update, invalid);
                    break;
                case ADDITION:
                case SUBTRACTION:
                    tryToEliminate(target, ZERO, false, source, original, update, invalid);
            }
            return true;
        }
        return false;
    }

    private void tryToEliminate(Variable elimination, Variable replacement,
                                boolean left, BinaryOperation target, double original,
                                Function<Expression, Expression> update, Counter invalid) {
        if (equal.test(replacement.value(), elimination.value())) {
            return;
        }

        var modified = update.apply(new BinaryOperation(
                left ? replacement : target.getLeft(),
                !left ? replacement : target.getRight(),
                target.getOperator()
        ));

        if (equal.test(modified.value(), original)) {
            invalid.count++;
        }
    }

    static final class Counter {
        int count;
    }
}
