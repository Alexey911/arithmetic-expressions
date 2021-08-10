package com.zhytnik.algo.brand.filter;

import com.zhytnik.algo.brand.compute.Transformation;
import com.zhytnik.algo.brand.data.Expression;
import com.zhytnik.algo.brand.filter.ast.AstNormalization;
import com.zhytnik.algo.brand.filter.ast.Tree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public final class AstUniqueExpressions implements Transformation {

    @Override
    public long complexity(int sourceSize) {
        return sourceSize;
    }

    @Override
    public List<Expression> apply(List<Expression> source) {
        var unique = new HashSet<Expression>();
        var normalization = new AstNormalization();

        for (var expression : source) {
            var tree = new Tree(expression);
            var normalized = normalization.apply(tree);
            var result = normalized.toExpression();

            unique.add(result);
        }
        return new ArrayList<>(unique);
    }
}
