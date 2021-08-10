package com.zhytnik.algo.brand.filter.ast;

import com.zhytnik.algo.brand.data.Expression;

import java.util.Objects;

public final class Tree {

    Node root;

    public Tree(Expression expression) {
        this(Node.toNode(expression));
    }

    public Tree(Node root) {
        this.root = Objects.requireNonNull(root);
    }

    public Expression toExpression() {
        return root.toExpression();
    }

    @Override
    public String toString() {
        return root.toString();
    }
}
