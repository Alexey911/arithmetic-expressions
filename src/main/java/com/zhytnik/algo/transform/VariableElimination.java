package com.zhytnik.algo.transform;

import com.zhytnik.algo.math.BinaryOperation;
import com.zhytnik.algo.math.Expression;
import com.zhytnik.algo.math.Variable;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;

@AllArgsConstructor
public class VariableElimination implements Transformation {

    private static final Variable ONE = new Variable("erased-value", 1.0);
    private static final Variable ZERO = new Variable("erased-value", 0.0);

    private final BiPredicate<Double, Double> equal;

    @Override
    public long expectedCount(int size) {
        return size;
    }

    @Override
    public List<Expression> apply(List<Expression> source) {
        Set<Expression> significant = new HashSet<>(source);

        for (var expression : source) {
            for (var variable : expression.variables()) {
                traverse(expression, Function.identity(), variable, expression, significant);
            }
        }
        System.out.println("Eliminated: " + (source.size() - significant.size()));
        return new ArrayList<>(significant);
    }

    private boolean traverse(Expression expression, Function<Expression, Expression> update,
                             Variable target, Expression original, Set<Expression> significant) {
        if (!(expression instanceof BinaryOperation)) {
            return false;
        }

        var op = (BinaryOperation) expression;

        boolean found = tryToEliminate(target, op, original, update, significant);

        if (!found) {
            found = traverse(
                    op.getLeft(),
                    modified -> update.apply(new BinaryOperation(modified, op.getRight(), op.getOperator())),
                    target, original, significant
            );
        }
        if (!found) {
            found = traverse(
                    op.getRight(),
                    modified -> update.apply(new BinaryOperation(op.getLeft(), modified, op.getOperator())),
                    target, original, significant
            );
        }
        return found;
    }

    private boolean tryToEliminate(Variable target, BinaryOperation source, Expression original,
                                   Function<Expression, Expression> update, Set<Expression> significant) {
        if (source.getLeft() == target) {
            switch (source.getOperator()) {
                case MULTIPLICATION:
                    tryToEliminate(target, ONE, true, source, original, update, significant);
                    break;
                case ADDITION:
                case SUBTRACTION:
                case DIVISION:
                    tryToEliminate(target, ZERO, true, source, original, update, significant);
            }
            return true;
        } else if (source.getRight() == target) {
            switch (source.getOperator()) {
                case DIVISION:
                case MULTIPLICATION:
                    tryToEliminate(target, ONE, false, source, original, update, significant);
                    break;
                case ADDITION:
                case SUBTRACTION:
                    tryToEliminate(target, ZERO, false, source, original, update, significant);
            }
            return true;
        }
        return false;
    }

    private void tryToEliminate(Variable elimination, Variable replacement,
                                boolean left, BinaryOperation target, Expression source,
                                Function<Expression, Expression> update, Set<Expression> significant) {
        if (equal.test(replacement.value(), elimination.value())) {
            return;
        }

        var modified = update.apply(new BinaryOperation(
                left ? replacement : target.getLeft(),
                !left ? replacement : target.getRight(),
                target.getOperator()
        ));

        if (equal.test(modified.value(), source.value())) {
            significant.remove(source);
        }
    }
}
