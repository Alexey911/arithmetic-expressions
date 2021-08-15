package com.zhytnik.algo.brand.filter.ast;

import com.zhytnik.algo.brand.data.BinaryOperator;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.UnaryOperator;

import static com.zhytnik.algo.brand.data.BinaryOperator.DIVISION;
import static com.zhytnik.algo.brand.data.BinaryOperator.MULTIPLICATION;
import static com.zhytnik.algo.brand.data.BinaryOperator.SUBTRACTION;

public final class SignSimplification implements UnaryOperator<Tree> {

    private final Comparator<Node> comparator;

    public SignSimplification() {
        this(SubtractionNodeComparator.INSTANCE);
    }

    public SignSimplification(Comparator<Node> comparator) {
        this.comparator = Objects.requireNonNull(comparator);
    }

    @Override
    public Tree apply(Tree tree) {
        traverse(tree.root);
        return tree;
    }

    private void traverse(Node node) {
        if (node.isVariable()) {
            return;
        }

        if (isSignNeutralOperator(node.operator)) {
            var left = biggestSubtraction(node.left);
            var right = biggestSubtraction(node.right);

            if (left == null && right == null) {
                return;
            }
            if (left != null & right != null && comparator.compare(left, right) > 0) {
                swap(left, right);
            }
        }

        traverse(node.left);
        traverse(node.right);
    }

    private Node biggestSubtraction(Node node) {
        var op = node.operator;

        if (op == SUBTRACTION) {
            return node;
        }
        if (isSignNeutralOperator(op)) {
            var left = biggestSubtraction(node.left);
            var right = biggestSubtraction(node.right);
            return (left != null && (right == null || comparator.compare(left, right) < 0)) ? left : right;
        }
        return null;
    }

    private boolean isSignNeutralOperator(BinaryOperator operator) {
        return operator == MULTIPLICATION || operator == DIVISION;
    }

    private void swap(Node left, Node right) {
        left.swapSides();
        right.swapSides();
    }
}
