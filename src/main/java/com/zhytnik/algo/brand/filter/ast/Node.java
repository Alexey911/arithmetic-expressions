package com.zhytnik.algo.brand.filter.ast;

import com.zhytnik.algo.brand.data.BinaryOperation;
import com.zhytnik.algo.brand.data.BinaryOperator;
import com.zhytnik.algo.brand.data.Expression;
import com.zhytnik.algo.brand.data.Variable;
import lombok.AllArgsConstructor;

import static java.util.Objects.nonNull;

@AllArgsConstructor
public final class Node {

    Node left;
    Node right;

    final Variable variable;
    BinaryOperator operator;

    boolean isVariable() {
        return nonNull(variable);
    }

    boolean isOperation() {
        return nonNull(operator);
    }

    int variables() {
        return variables(this);
    }

    void swapSides() {
        var tmp = left;
        left = right;
        right = tmp;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    StringBuilder description() {
        var sb = new StringBuilder();
        print(this, sb);
        return sb;
    }

    @Override
    public String toString() {
        return isVariable() ? variable.formatted() : format(left) + operator.getDisplay() + format(right);
    }

    private String format(Node node) {
        return node.isVariable() ? node.toString() : "(" + node.toString() + ")";
    }

    public Expression toExpression() {
        return toExpression(this);
    }

    static Node variable(Variable variable) {
        return new Node(null, null, variable, null);
    }

    static Node operation(Node left, Node right, BinaryOperator operator) {
        return new Node(left, right, null, operator);
    }

    static Node toNode(Expression expression) {
        if (!(expression instanceof BinaryOperation)) {
            return Node.variable((Variable) expression);
        }

        var op = (BinaryOperation) expression;

        return Node.operation(
                toNode(op.getLeft()),
                toNode(op.getRight()),
                op.getOperator()
        );
    }

    private static int variables(Node node) {
        return node.isVariable() ? 1 : variables(node.left) + variables(node.right);
    }

    private static Expression toExpression(Node node) {
        if (node.isVariable()) {
            return node.variable;
        } else {
            return new BinaryOperation(
                    toExpression(node.left),
                    toExpression(node.right),
                    node.operator
            );
        }
    }

    private static void print(Node node, StringBuilder target) {
        if (node.isVariable()) {
            target.append(node.variable.formatted());
        } else {
            print(node.left, target);
            print(node.right, target);
        }
    }
}
