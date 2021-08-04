package com.zhytnik.algo.brand.filter.ast;

import java.util.function.UnaryOperator;

import static com.zhytnik.algo.brand.data.BinaryOperator.ADDITION;
import static com.zhytnik.algo.brand.data.BinaryOperator.SUBTRACTION;

final class SubtractionSeparation implements UnaryOperator<Tree> {

    @Override
    public Tree apply(Tree tree) {
        tryToReplaceSubByAdd(tree.root);
        return tree;
    }

    private void tryToReplaceSubByAdd(Node node) {
        if (node.left.isOperation()) {
            tryToReplaceSubByAdd(node.left);
        }
        if (node.right.isOperation()) {
            tryToReplaceSubByAdd(node.right);
        }

        if (node.operator == SUBTRACTION && node.left.operator == SUBTRACTION && node.right.operator == SUBTRACTION) {
            replaceTrio(node);
        }
        if (node.operator == ADDITION && node.left.operator == SUBTRACTION && node.right.operator == SUBTRACTION) {
            replacePairSub(node);
        }
        if (node.operator == ADDITION && node.left.operator == SUBTRACTION) {
            replaceLeftSub(node);
        }
        if (node.operator == ADDITION && node.right.operator == SUBTRACTION) {
            replaceRightSub(node);
        }
        if (node.operator == SUBTRACTION && node.right.operator == SUBTRACTION) {
            replaceRightNestedSub(node);
        }
        if (node.operator == SUBTRACTION && node.left.operator == SUBTRACTION) {
            replaceLeftNestedSub(node);
        }
    }

    private void replaceTrio(Node node) {
        var tmp = node.left;
        node.left = addition(tmp.left, node.right.right);
        node.right = addition(tmp.right, node.right.left);
    }

    private void replacePairSub(Node node) {
        node.operator = SUBTRACTION;
        var tmp = node.left;
        node.left = addition(tmp.left, node.right.left);
        node.right = addition(tmp.right, node.right.right);
    }

    private void replaceLeftSub(Node node) {
        node.operator = SUBTRACTION;
        var tmp = node.left;
        node.left = addition(tmp.left, node.right);
        node.right = tmp.right;
    }

    private void replaceRightSub(Node node) {
        node.operator = SUBTRACTION;
        node.left = addition(node.left, node.right.left);
        node.right = node.right.right;
    }

    private void replaceRightNestedSub(Node node) {
        node.left = addition(node.left, node.right.right);
        node.right = node.right.left;
    }

    private void replaceLeftNestedSub(Node node) {
        node.right = addition(node.left.right, node.right);
        node.left = node.left.left;
    }

    private Node addition(Node left, Node right) {
        return left.variables() >= right.variables() ?
                Node.operation(left, right, ADDITION) : Node.operation(right, left, ADDITION);
    }
}
