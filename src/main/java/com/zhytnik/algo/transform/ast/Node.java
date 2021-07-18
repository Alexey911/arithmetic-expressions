package com.zhytnik.algo.transform.ast;

import com.zhytnik.algo.math.BinaryOperation;
import com.zhytnik.algo.math.BinaryOperator;
import com.zhytnik.algo.math.Expression;
import com.zhytnik.algo.math.Variable;
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

    StringBuilder description() {
        var sb = new StringBuilder();
        print(this, sb);
        return sb;
    }

    @Override
    public String toString() {
        return isVariable() ? variable.writtenFormat() : "(" + left.toString() + ")" + operator.getSymbol() + "(" + right.toString() + ")";
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
            target.append(node.variable.writtenFormat());
        } else {
            print(node.left, target);
            print(node.right, target);
        }
    }
}
