package com.zhytnik.algo.brand.filter;

import com.zhytnik.algo.brand.compute.Transformation;
import com.zhytnik.algo.brand.data.BinaryOperation;
import com.zhytnik.algo.brand.data.Expression;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
public class UniqueExpressions implements Transformation {

    private final int complexity;

    @Override
    public long complexity(int outputSize) {
        return Math.multiplyFull(outputSize, complexity - 1);
    }

    @Override
    public List<Expression> apply(List<Expression> source) {
        Set<Expression> ignore = new HashSet<>();
        Set<Expression> unique = new HashSet<>(source);

        for (var expression : source) {
            removeEquivalents(expression, expression, unique, ignore);
            ignore.add(expression);
        }
        return new ArrayList<>(unique);
    }

    private void removeEquivalents(Expression target, Expression source,
                                   Set<Expression> unique, Set<Expression> ignore) {
        if (!(target instanceof BinaryOperation)) {
            return;
        }

        var op = (BinaryOperation) target;

        if (op.getOperator().isCommutative()) {
            var equivalent = source.recalculateWith(Map.of(op, new BinaryOperation(op.getRight(), op.getLeft(), op.getOperator())));

            if (!ignore.contains(equivalent)) {
                unique.remove(equivalent);
            }
        }

        removeEquivalents(op.getLeft(), source, unique, ignore);
        removeEquivalents(op.getRight(), source, unique, ignore);
    }
}
