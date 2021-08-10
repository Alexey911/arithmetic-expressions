package com.zhytnik.algo.brand.filter;

import com.zhytnik.algo.brand.compute.Transformation;
import com.zhytnik.algo.brand.data.Expression;
import com.zhytnik.algo.brand.filter.ast.AstNormalization;
import com.zhytnik.algo.brand.filter.ast.Tree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public final class AstUniqueExpressions implements Transformation {

    private final AstNormalization normalization;

    public AstUniqueExpressions() {
        this(new AstNormalization());
    }

    public AstUniqueExpressions(AstNormalization normalization) {
        this.normalization = Objects.requireNonNull(normalization);
    }

    @Override
    public long complexity(int sourceSize) {
        return Math.multiplyFull(sourceSize, normalization.stepCount());
    }

    @Override
    public List<Expression> apply(List<Expression> source) {
        var unique = new HashSet<Expression>();

        for (var expression : source) {
            var tree = new Tree(expression);
            var normalized = normalization.apply(tree);
            var result = normalized.toExpression();

            unique.add(result);
        }
        return new ArrayList<>(unique);
    }
}
