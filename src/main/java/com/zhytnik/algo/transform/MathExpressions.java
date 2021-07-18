package com.zhytnik.algo.transform;

import com.zhytnik.algo.math.BinaryOperation;
import com.zhytnik.algo.math.BinaryOperator;
import com.zhytnik.algo.math.Expression;

import java.util.ArrayList;
import java.util.List;

public class MathExpressions implements Transformation {

    private static final int[] EXPECTED_SIZE = {0, 0, 4, 32, 320, 3584, 43_008, 540_672, 7_028_736};

    private static final BinaryOperator[] BINARY_OPERATORS = BinaryOperator.values();

    @Override
    public long expectedCount(int size) {
        return EXPECTED_SIZE[size];
    }

    @Override
    public List<Expression> apply(List<Expression> source) {
        int count = source.size();

        if (count < 2 || count > 8) {
            throw new IllegalArgumentException("Source size should have value from [2, 8] range! Actual is " + count);
        }
        return expressions(source, 0, count);
    }

    private List<Expression> expressions(List<Expression> source, int from, int to) {
        int total = to - from;

        if (total == 1) {
            return List.of(source.get(from));
        } else if (total == 2) {
            return allBiCombinations(source.get(from), source.get(from + 1));
        }

        List<Expression> result = new ArrayList<>(EXPECTED_SIZE[total]);

        for (int i = 1; i < total; i++) {
            var left = expressions(source, from, from + i);
            var right = expressions(source, from + i, to);

            fillByAllBiCombinations(result, left, right);
        }
        return result;
    }

    private List<Expression> allBiCombinations(Expression left,
                                               Expression right) {
        List<Expression> result = new ArrayList<>(BINARY_OPERATORS.length);

        for (var op : BINARY_OPERATORS) {
            result.add(new BinaryOperation(left, right, op));
        }
        return result;
    }

    private void fillByAllBiCombinations(List<Expression> target,
                                         List<Expression> lefts,
                                         List<Expression> rights) {
        for (var op : BINARY_OPERATORS) {
            for (var left : lefts) {
                for (var right : rights) {
                    target.add(new BinaryOperation(left, right, op));
                }
            }
        }
    }
}
