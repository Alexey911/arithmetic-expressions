package com.zhytnik.algo.brand.filter;

import com.zhytnik.algo.brand.compute.Transformation;
import com.zhytnik.algo.brand.data.Expression;
import com.zhytnik.algo.brand.threshold.Threshold;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class MathErrorVerification implements Transformation {

    private final double expected;
    private final Threshold similarity;

    public MathErrorVerification(Expression target, Threshold similarity) {
        this(target.value(), similarity);
    }

    public MathErrorVerification(double expected, Threshold similarity) {
        this.expected = expected;
        this.similarity = Objects.requireNonNull(similarity);
    }

    @Override
    public String shortName() {
        return "Verification";
    }

    @Override
    public long complexity(int sourceSize) {
        return sourceSize;
    }

    @Override
    public List<Expression> apply(List<Expression> source) {
        var illegal = new ArrayList<Expression>();

        for (var e : source) {
            if (!similarity.isAcceptable(e.value(), expected)) {
                illegal.add(e);
            }
        }

        if (!illegal.isEmpty()) {
            throw new IllegalStateException("Some valid expressions have illegal values! Non-valid expressions are: " + illegal);
        }
        return source;
    }
}
