package com.zhytnik.algo.brand.gen;

import com.zhytnik.algo.brand.data.BinaryOperation;
import com.zhytnik.algo.brand.data.BinaryOperator;
import com.zhytnik.algo.brand.data.Expression;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
final class AllArithmeticPermutations {

    private static final int[] EXPECTED_SIZE = {0, 0, 4, 32, 320, 3584, 43_008, 540_672, 7_028_736};

    private static final BinaryOperator[] BINARY_OPERATORS = BinaryOperator.values();
    private static final int BINARY_OPERATORS_LENGTH = BINARY_OPERATORS.length;

    public static long permutationsCount(int sourceSize) {
        return EXPECTED_SIZE[sourceSize];
    }

    public static Expression[] computeAllPermutations(List<Expression> source) {
        int total = source.size();

        if (total < 2 || total > 8) {
            throw new IllegalArgumentException("Source size should have value from [2, 8] range! Actual is " + total);
        }
        return expressions(source, 0, total);
    }

    private static Expression[] expressions(List<Expression> source, int from, int to) {
        int total = to - from;

        if (total == 1) {
            return new Expression[]{source.get(from)};
        } else if (total == 2) {
            return allBiCombinations(source.get(from), source.get(from + 1));
        }
        return fill(source, from, to, total);
    }

    private static Expression[] fill(List<Expression> source, int from, int to, int total) {
        Expression[] result = new Expression[EXPECTED_SIZE[total]];

        for (int i = 1, offset = 0; i < total; i++) {
            var left = expressions(source, from, from + i);
            var right = expressions(source, from + i, to);

            fillByAllBiCombinations(result, left, right, offset);
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

    private static void fillByAllBiCombinations(Expression[] target, Expression[] lefts, Expression[] rights, int offset) {
        for (int k = 0; k < BINARY_OPERATORS_LENGTH; k++) {
            var op = BINARY_OPERATORS[k];
            int leftSize = lefts.length, rightSize = rights.length, val = offset + k * leftSize * rightSize;

            for (int i = 0; i < leftSize; i++) {
                var left = lefts[i];

                for (int j = 0; j < rightSize; j++) {
                    target[val + i * rightSize + j] = new BinaryOperation(left, rights[j], op);
                }
            }
        }
    }
}
