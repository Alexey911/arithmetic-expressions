package com.zhytnik.algo.brand.filter.ast;

import java.util.function.UnaryOperator;

import static com.zhytnik.algo.brand.data.BinaryOperator.DIVISION;
import static com.zhytnik.algo.brand.data.BinaryOperator.MULTIPLICATION;
import static com.zhytnik.algo.brand.data.BinaryOperator.SUBTRACTION;

public final class SignSimplification implements UnaryOperator<Tree> {

    private static final String SPLITERATOR = ",";

    //TODO(zhytnik): revert when related to internal
    @Override
    public Tree apply(Tree tree) {
        traverse(tree.root);
        return tree;
    }

    private void traverse(Node node) {
        if (node.isVariable()) {
            return;
        }

        if ((node.operator == MULTIPLICATION || node.operator == DIVISION) &&
                node.left.operator == SUBTRACTION && node.right.operator == SUBTRACTION) {
            tryToSimplify(node);
        }

        traverse(node.left);
        traverse(node.right);
    }

    private void tryToSimplify(Node node) {
        int leftLength = node.left.left.variables() + node.right.left.variables();
        int rightLength = node.left.right.variables() + node.right.right.variables();

        if (leftLength < rightLength) {
            swap(node);
        } else if (leftLength == rightLength) {
            var left = concat(node.left.left, node.right.left);
            var right = concat(node.left.right, node.right.right);

            if (left.length() < right.length() || (left.length() == right.length() && toScore(left) > toScore(right))) {
                swap(node);
            }
        }
    }

    private StringBuilder concat(Node first, Node second) {
        return first.description(SPLITERATOR).append(SPLITERATOR).append(second.description(SPLITERATOR));
    }

    private double toScore(StringBuilder description) {
        double score = 1;

        var variables = description.toString().split(SPLITERATOR);

        for (var variable : variables) {
            int sum = 0;

            for (int i = 0, size = variable.length(); i < size; i++) {
                sum += variable.charAt(i);
            }
            score *= sum;
        }
        return score;
    }

    private void swap(Node node) {
        node.left.swapSides();
        node.right.swapSides();
    }
}
