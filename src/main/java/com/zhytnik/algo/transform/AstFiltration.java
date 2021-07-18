package com.zhytnik.algo.transform;

import com.zhytnik.algo.math.Expression;
import com.zhytnik.algo.transform.ast.Tree;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.UnaryOperator;

@AllArgsConstructor
public class AstFiltration implements Transformation {

    private final UnaryOperator<Tree> normalization;

    @Override
    public long expectedCount(int size) {
        return size;
    }

    @Override
    public List<Expression> apply(List<Expression> source) {
        Set<Expression> unique = new HashSet<>();

        for (var expression : source) {
            var tree = Tree.toTree(expression);
            var normalized = normalization.apply(tree);
            var result = normalized.getRoot().toExpression();

            unique.add(result);
        }
        System.out.println("AST normalized: " + (source.size() - unique.size()));
        return new ArrayList<>(unique);
    }
}
