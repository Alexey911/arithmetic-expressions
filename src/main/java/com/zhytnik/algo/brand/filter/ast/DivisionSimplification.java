package com.zhytnik.algo.brand.filter.ast;

import java.util.function.UnaryOperator;

import static com.zhytnik.algo.brand.data.BinaryOperator.DIVISION;
import static com.zhytnik.algo.brand.data.BinaryOperator.MULTIPLICATION;

final class DivisionSimplification implements UnaryOperator<Tree> {

    @Override
    public Tree apply(Tree tree) {
        travers(tree.root);
        return tree;
    }

    private void travers(Node node) {
        if (node.left.isOperation()) {
            travers(node.left);
        }

        if (node.right.isOperation()) {
            travers(node.right);
        }

        if (node.operator == MULTIPLICATION) {
            if (node.left.operator == DIVISION) {
                node.operator = DIVISION;

                if (node.right.operator == DIVISION) {
                    combineDivs(node);
                } else {
                    leftToMultiplication(node);
                }
            } else if (node.right.operator == DIVISION) {
                node.operator = DIVISION;
                toMultiplicationRight(node, true);
            }
        } else if (node.operator == DIVISION) {
            if (node.left.operator == DIVISION) {
                rightToMultiplication(node);
            } else if (node.right.operator == DIVISION) {
                toMultiplicationRight(node, false);
            }
        }
    }

    private void combineDivs(Node node) {
        var left = node.left;
        node.left = Node.operation(left.left, node.right.left, MULTIPLICATION);
        node.right = Node.operation(left.right, node.right.right, MULTIPLICATION);
    }

    private void leftToMultiplication(Node node) {
        var left = node.left;
        node.left = Node.operation(left.left, node.right, MULTIPLICATION);
        node.right = left.right;
    }

    private void rightToMultiplication(Node node) {
        node.right = Node.operation(node.left.right, node.right, MULTIPLICATION);
        node.left = node.left.left;
    }

    private void toMultiplicationRight(Node node, boolean saveOrder) {
        node.left = Node.operation(
                node.left,
                saveOrder ? node.right.left : node.right.right,
                MULTIPLICATION
        );
        node.right = saveOrder ? node.right.right : node.right.left;
    }
}
