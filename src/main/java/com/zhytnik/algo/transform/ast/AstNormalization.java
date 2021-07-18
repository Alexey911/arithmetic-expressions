package com.zhytnik.algo.transform.ast;

import com.zhytnik.algo.math.BinaryOperator;

import java.util.function.UnaryOperator;

import static com.zhytnik.algo.math.BinaryOperator.ADDITION;
import static com.zhytnik.algo.math.BinaryOperator.DIVISION;
import static com.zhytnik.algo.math.BinaryOperator.MULTIPLICATION;
import static com.zhytnik.algo.math.BinaryOperator.SUBTRACTION;

public final class AstNormalization implements UnaryOperator<Tree> {

    @Override
    public Tree apply(Tree tree) {
        moveLongestToTheLeftSide(tree.root);
        tryToTrim(tree.root);
        tryToReplaceSubByAdd(tree.root);
        tryToReplaceDivByMultiplication(tree.root);
        moveLongestToTheLeftSide(tree.root);
        return tree;
    }

    private void tryToTrim(Node node) {
        if (node.left.isOperation() && node.left.left.isOperation()) {
            tryToTrim(node.left);

            tryToTrimRight(node, ADDITION, ADDITION, ADDITION, ADDITION, true);
            tryToTrimRight(node, ADDITION, SUBTRACTION, ADDITION, SUBTRACTION, false);
            tryToTrimRight(node, DIVISION, DIVISION, DIVISION, MULTIPLICATION, true);
            tryToTrimRight(node, SUBTRACTION, ADDITION, ADDITION, SUBTRACTION, true);
            tryToTrimRight(node, SUBTRACTION, SUBTRACTION, SUBTRACTION, ADDITION, true);
            tryToTrimRight(node, MULTIPLICATION, MULTIPLICATION, MULTIPLICATION, MULTIPLICATION, true);
        }

        if (node.right.isOperation() && node.right.right.isOperation()) {
            tryToTrim(node.right);

            tryToTrimLeft(node, ADDITION, ADDITION, ADDITION, ADDITION, true);
            tryToTrimLeft(node, ADDITION, SUBTRACTION, ADDITION, SUBTRACTION, true);
            tryToTrimLeft(node, SUBTRACTION, ADDITION, SUBTRACTION, SUBTRACTION, true);
            tryToTrimLeft(node, SUBTRACTION, SUBTRACTION, ADDITION, SUBTRACTION, true);
            tryToTrimLeft(node, MULTIPLICATION, MULTIPLICATION, MULTIPLICATION, MULTIPLICATION, true);
            tryToTrimLeft(node, DIVISION, DIVISION, DIVISION, MULTIPLICATION, false);
        }
    }

    private void tryToTrimRight(Node node,
                                BinaryOperator rootFrom, BinaryOperator leftFrom,
                                BinaryOperator rootTo, BinaryOperator target,
                                boolean saveOrder) {
        if (node.operator == rootFrom && node.left.operator == leftFrom && node.right.isVariable() && node.left.right.isVariable()) {
            node.operator = rootTo;
            node.right = Node.operation(
                    saveOrder ? node.left.right : node.right,
                    saveOrder ? node.right : node.left.right,
                    target
            );
            node.left = node.left.left;
        }
    }

    private void tryToTrimLeft(Node node,
                               BinaryOperator rootFrom, BinaryOperator rightFrom,
                               BinaryOperator rootTo, BinaryOperator target,
                               boolean saveOrder) {
        if (node.operator == rootFrom && node.right.operator == rightFrom && node.left.isVariable() && node.right.left.isVariable()) {
            node.operator = rootTo;
            node.left = Node.operation(
                    saveOrder ? node.left : node.right.left,
                    saveOrder ? node.right.left : node.left,
                    target
            );
            node.right = node.right.right;
        }
    }

    private void moveLongestToTheLeftSide(Node node) {
        boolean leftVariable = node.left.isVariable();
        boolean rightVariable = node.right.isVariable();

        if (!leftVariable) {
            moveLongestToTheLeftSide(node.left);
        }
        if (!rightVariable) {
            moveLongestToTheLeftSide(node.right);
        }

        if (node.operator.isCommutative()) {
            int left = node.left.variables();
            int right = node.right.variables();

            if (left < right) {
                node.swapSides();
            } else if (left == right) {
                var l = node.left.description();
                var r = node.right.description();

                if (l.length() < r.length() || l.compareTo(r) < 0) {
                    node.swapSides();
                }
            }
        }
    }

    private void tryToReplaceSubByAdd(Node node) {
        if (node.left.isOperation()) {
            tryToReplaceSubByAdd(node.left);
        }
        if (node.right.isOperation()) {
            if (node.right.left.isOperation()) {
                tryToReplaceSubByAdd(node.right);
            } else if (node.operator == SUBTRACTION && node.right.operator == SUBTRACTION && node.right.right.isVariable()) {
                node.operator = ADDITION;
                node.right.swapSides();
            }
        }
    }

    private void tryToReplaceDivByMultiplication(Node node) {
        if (node.left.isOperation()) {
            tryToReplaceDivByMultiplication(node.left);
        }

        if (node.right.isOperation()) {
            tryToReplaceDivByMultiplication(node.right);
        }

        if (node.operator == MULTIPLICATION) {
            if (node.left.operator == DIVISION) {
                node.operator = DIVISION;
                leftToMultiplication(node);
            }
            if (node.right.operator == DIVISION) {
                node.operator = DIVISION;
                toMultiplicationRight(node, true);
            }
        } else if (node.operator == DIVISION) {
            if (node.left.operator == DIVISION) {
                leftToMultiplication(node);
            }
            if (node.right.operator == DIVISION) {
                toMultiplicationRight(node, false);
            }
        }
    }

    private void leftToMultiplication(Node node) {
        var left = node.left;
        node.left = Node.operation(left.left, node.right, MULTIPLICATION);
        node.right = left.right;
    }

    private void toMultiplicationRight(Node node, boolean multiplication) {
        node.left = Node.operation(
                node.left,
                multiplication ? node.right.left : node.right.right,
                MULTIPLICATION
        );
        node.right = multiplication ? node.right.right : node.right.left;
    }
}
