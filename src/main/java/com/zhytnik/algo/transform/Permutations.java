package com.zhytnik.algo.transform;

import com.google.common.collect.Collections2;
import com.google.common.math.LongMath;
import com.zhytnik.algo.math.Expression;

import java.util.Collection;
import java.util.List;

public class Permutations implements GroupTransformation<Collection<Expression>, List<Expression>> {

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public Collection<List<Expression>> apply(Collection<Expression> source) {
        return Collections2.permutations(source);
    }

    @Override
    public long expectedCount(int size) {
        return LongMath.factorial(size);
    }
}
