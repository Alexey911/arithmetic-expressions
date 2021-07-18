package com.zhytnik.algo.transform.ast;

import com.zhytnik.algo.math.Expression;
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
