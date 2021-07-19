package com.zhytnik.algo.transform;

import com.zhytnik.algo.math.BinaryOperation;
import com.zhytnik.algo.math.BinaryOperator;
import com.zhytnik.algo.math.Expression;

import java.util.Arrays;
import java.util.List;

public final class MathExpressions implements Transformation {

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
        return Arrays.asList(expressions(source, 0, count));
    }

    private static Expression[] expressions(List<Expression> source, int from, int to) {
        int total = to - from;

        if (total == 1) {
            return new Expression[]{source.get(from)};
        } else if (total == 2) {
            return allBiCombinations(source.get(from), source.get(from + 1));
        }

        Expression[] result = new Expression[EXPECTED_SIZE[total]];

        for (int i = 1, offset = 0; i < total; i++) {
            var left = expressions(source, from, from + i);
            var right = expressions(source, from + i, to);

            fillByAllBiCombinations(result, offset, left, right);
            offset += BINARY_OPERATORS.length * left.length * right.length;
        }
        return result;
    }

    private static Expression[] allBiCombinations(Expression left, Expression right) {
        return new Expression[]{
                new BinaryOperation(left, right, BinaryOperator.DIVISION),
                new BinaryOperation(left, right, BinaryOperator.ADDITION),
                new BinaryOperation(left, right, BinaryOperator.SUBTRACTION),
                new BinaryOperation(left, right, BinaryOperator.MULTIPLICATION)
        };
    }

    private static void fillByAllBiCombinations(Expression[] target, int offset, Expression[] lefts, Expression[] rights) {
        int leftSize = lefts.length, rightSize = rights.length;

        for (int k = 0, ops = BINARY_OPERATORS.length; k < ops; k++) {
            var op = BINARY_OPERATORS[k];

            for (int i = 0; i < leftSize; i++) {
                var left = lefts[i];

                for (int j = 0; j < rightSize; j++) {
                    target[offset + (k * leftSize * rightSize) + (i * rightSize) + j] = new BinaryOperation(left, rights[j], op);
                }
            }
        }
    }
}
