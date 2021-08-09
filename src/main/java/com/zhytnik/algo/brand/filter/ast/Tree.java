package com.zhytnik.algo.brand.filter.ast;

import com.zhytnik.algo.brand.data.Expression;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
public final class Tree {

    @Getter
    Node root;

    public Tree(@NonNull Expression expression) {
        root = Node.toNode(expression);
    }

    public Expression toExpression() {
        return root.toExpression();
    }

    @Override
    public String toString() {
        return root.toString();
    }
}
