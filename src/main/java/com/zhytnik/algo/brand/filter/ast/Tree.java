package com.zhytnik.algo.brand.filter.ast;

import com.zhytnik.algo.brand.data.Expression;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class Tree {

    @Getter
    Node root;

    public static Tree toTree(Expression expression) {
        return new Tree(Node.toNode(expression));
    }

    @Override
    public String toString() {
        return root.toString();
    }
}
