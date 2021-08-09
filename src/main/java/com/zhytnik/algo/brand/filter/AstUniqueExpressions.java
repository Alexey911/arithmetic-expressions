package com.zhytnik.algo.brand.filter;

import com.zhytnik.algo.brand.compute.Transformation;
import com.zhytnik.algo.brand.data.Expression;
import com.zhytnik.algo.brand.filter.ast.AstNormalization;
import com.zhytnik.algo.brand.filter.ast.Tree;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class AstUniqueExpressions implements Transformation {

    @Override
    public long complexity(int outputSize) {
        return outputSize;
    }

    @Override
    public List<Expression> apply(List<Expression> source) {
        Set<Expression> unique = new HashSet<>();
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