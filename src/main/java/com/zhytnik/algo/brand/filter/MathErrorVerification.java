package com.zhytnik.algo.brand.filter;

import com.zhytnik.algo.brand.compute.Transformation;
import com.zhytnik.algo.brand.data.Expression;
import com.zhytnik.algo.brand.threshold.Threshold;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class MathErrorVerification implements Transformation {

    private final double target;
    private final Threshold similarity;

    public MathErrorVerification(Expression target, Threshold similarity) {
        this(target.value(), similarity);
    }

    public MathErrorVerification(double target, Threshold similarity) {
        this.target = target;
        this.similarity = Objects.requireNonNull(similarity);
    }

    @Override
    public long complexity(int sourceSize) {
        return sourceSize;
    }

    @Override
    public List<Expression> apply(List<Expression> source) {
        List<Expression> illegal = new ArrayList<>();

        for (var expression : source) {
            if (!similarity.isAcceptable(expression.value(), target)) {
                illegal.add(expression);
            }
        }

        if (!illegal.isEmpty()) {
            throw new IllegalStateException("Some valid expressions have illegal values! Non-valid expressions are: " + illegal);
        }
        return source;
    }
}
