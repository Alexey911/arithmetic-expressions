package com.zhytnik.algo.transform;

import com.zhytnik.algo.math.BinaryOperation;
import com.zhytnik.algo.math.Expression;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class UniqueExpressions implements Transformation {

    @Override
    public long expectedCount(int size) {
        return size;
    }

    @Override
    public List<Expression> apply(List<Expression> source) {
        Set<Expression> ignore = new HashSet<>();
        Set<Expression> unique = new HashSet<>(source);

        for (var expression : source) {
            removeEquivalents(expression, Function.identity(), unique, ignore);
            ignore.add(expression);
        }

        System.out.println("Duplicates: " + (source.size() - unique.size()));
        return new ArrayList<>(unique);
    }

    private void removeEquivalents(Expression target,
                                   Function<Expression, Expression> update,
                                   Set<Expression> unique, Set<Expression> ignore) {
        if (!(target instanceof BinaryOperation)) {
            return;
        }

        var op = (BinaryOperation) target;

        if (op.getOperator().isCommutative()) {
            var equivalent = update.apply(new BinaryOperation(op.getRight(), op.getLeft(), op.getOperator()));

            if (!ignore.contains(equivalent)) {
                unique.remove(equivalent);
            }
        }

        removeEquivalents(op.getLeft(), modified -> update.apply(new BinaryOperation(modified, op.getRight(), op.getOperator())), unique, ignore);
        removeEquivalents(op.getRight(), modified -> update.apply(new BinaryOperation(op.getLeft(), modified, op.getOperator())), unique, ignore);
    }
}
