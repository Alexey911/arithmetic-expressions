package com.zhytnik.algo.brand.filter.ast;

import com.zhytnik.algo.brand.data.BinaryOperator;

import java.util.function.UnaryOperator;

import static com.zhytnik.algo.brand.data.BinaryOperator.ADDITION;
import static com.zhytnik.algo.brand.data.BinaryOperator.DIVISION;
import static com.zhytnik.algo.brand.data.BinaryOperator.MULTIPLICATION;
import static com.zhytnik.algo.brand.data.BinaryOperator.SUBTRACTION;

final class BinaryGrouping implements UnaryOperator<Tree> {

    @Override
    public Tree apply(Tree tree) {
        traverse(tree.root);
        return tree;
    }

    private void traverse(Node node) {
        if (node.left.isOperation()) {
            traverse(node.left);

            if (node.right.isVariable() && node.variables() > 3) {
                tryToTrimLeftSide(node);
            }
        }
        if (node.right.isOperation()) {
            traverse(node.right);

            if (node.left.isVariable() && node.variables() > 3) {
                tryToTrimRightSide(node);
            }
        }
    }

    private void tryToTrimLeftSide(Node node) {
        tryToTrimLeftLeft(node, ADDITION, ADDITION, ADDITION, ADDITION);
        tryToTrimLeftLeft(node, ADDITION, SUBTRACTION, SUBTRACTION, ADDITION);
        tryToTrimLeftLeft(node, DIVISION, DIVISION, DIVISION, DIVISION);
        tryToTrimLeftLeft(node, DIVISION, MULTIPLICATION, MULTIPLICATION, DIVISION);
        tryToTrimLeftLeft(node, SUBTRACTION, ADDITION, ADDITION, SUBTRACTION);
        tryToTrimLeftLeft(node, SUBTRACTION, SUBTRACTION, SUBTRACTION, SUBTRACTION);
        tryToTrimLeftLeft(node, MULTIPLICATION, MULTIPLICATION, MULTIPLICATION, MULTIPLICATION);
        tryToTrimLeftLeft(node, MULTIPLICATION, DIVISION, DIVISION, MULTIPLICATION);

        tryToTrimLeftRight(node, DIVISION, DIVISION, DIVISION, MULTIPLICATION, true);
        tryToTrimLeftRight(node, DIVISION, MULTIPLICATION, MULTIPLICATION, DIVISION, true);
        tryToTrimLeftRight(node, ADDITION, ADDITION, ADDITION, ADDITION, true);
        tryToTrimLeftRight(node, ADDITION, SUBTRACTION, ADDITION, SUBTRACTION, false);
        tryToTrimLeftRight(node, SUBTRACTION, ADDITION, ADDITION, SUBTRACTION, true);
        tryToTrimLeftRight(node, SUBTRACTION, SUBTRACTION, SUBTRACTION, ADDITION, true);
        tryToTrimLeftRight(node, MULTIPLICATION, MULTIPLICATION, MULTIPLICATION, MULTIPLICATION, true);
        tryToTrimLeftRight(node, MULTIPLICATION, DIVISION, MULTIPLICATION, DIVISION, false);
    }

    private void tryToTrimRightSide(Node node) {
        tryToTrimRightLeft(node, DIVISION, DIVISION, MULTIPLICATION, DIVISION);
        tryToTrimRightLeft(node, DIVISION, MULTIPLICATION, DIVISION, DIVISION);
        tryToTrimRightLeft(node, ADDITION, ADDITION, ADDITION, ADDITION);
        tryToTrimRightLeft(node, ADDITION, SUBTRACTION, SUBTRACTION, ADDITION);
        tryToTrimRightLeft(node, SUBTRACTION, ADDITION, SUBTRACTION, SUBTRACTION);
        tryToTrimRightLeft(node, SUBTRACTION, SUBTRACTION, ADDITION, SUBTRACTION);
        tryToTrimRightLeft(node, MULTIPLICATION, MULTIPLICATION, MULTIPLICATION, MULTIPLICATION);
        tryToTrimRightLeft(node, MULTIPLICATION, DIVISION, DIVISION, MULTIPLICATION);

        tryToTrimRightRight(node, DIVISION, DIVISION, DIVISION, MULTIPLICATION);
        tryToTrimRightRight(node, DIVISION, MULTIPLICATION, DIVISION, DIVISION);
        tryToTrimRightRight(node, ADDITION, ADDITION, ADDITION, ADDITION);
        tryToTrimRightRight(node, ADDITION, SUBTRACTION, ADDITION, SUBTRACTION);
        tryToTrimRightRight(node, SUBTRACTION, ADDITION, SUBTRACTION, SUBTRACTION);
        tryToTrimRightRight(node, SUBTRACTION, SUBTRACTION, SUBTRACTION, ADDITION);
        tryToTrimRightRight(node, MULTIPLICATION, DIVISION, MULTIPLICATION, DIVISION);
        tryToTrimRightRight(node, MULTIPLICATION, MULTIPLICATION, MULTIPLICATION, MULTIPLICATION);
    }

    private void tryToTrimLeftLeft(Node node,
                                   BinaryOperator rootFrom, BinaryOperator leftFrom,
                                   BinaryOperator rootTo, BinaryOperator target) {
        if (node.operator == rootFrom && node.left.operator == leftFrom && node.right.isVariable() && node.left.left.isVariable()) {
            node.operator = rootTo;

            var tmp = node.left.right;
            node.left = Node.operation(node.left.left, node.right, target);
            node.right = tmp;
        }
    }

    private void tryToTrimLeftRight(Node node,
                                    BinaryOperator rootFrom, BinaryOperator leftFrom,
                                    BinaryOperator rootTo, BinaryOperator target,
                                    boolean keepOrder) {
        if (node.operator == rootFrom && node.left.operator == leftFrom && node.right.isVariable() && node.left.right.isVariable()) {
            node.operator = rootTo;
            node.right = Node.operation(
                    keepOrder ? node.left.right : node.right,
                    keepOrder ? node.right : node.left.right,
                    target
            );
            node.left = node.left.left;
        }
    }

    private void tryToTrimRightLeft(Node node,
                                    BinaryOperator rootFrom, BinaryOperator rightFrom,
                                    BinaryOperator rootTo, BinaryOperator target) {
        if (node.operator == rootFrom && node.right.operator == rightFrom && node.left.isVariable() && node.right.left.isVariable()) {
            node.operator = rootTo;
            node.left = Node.operation(node.left, node.right.left, target);
            node.right = node.right.right;
        }
    }

    private void tryToTrimRightRight(Node node,
                                     BinaryOperator rootFrom, BinaryOperator rightFrom,
                                     BinaryOperator rootTo, BinaryOperator target) {
        if (node.operator == rootFrom && node.right.operator == rightFrom && node.left.isVariable() && node.right.right.isVariable()) {
            node.operator = rootTo;
            node.left = Node.operation(node.left, node.right.right, target);
            node.right = node.right.left;
        }
    }
}
