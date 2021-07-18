package com.zhytnik.algo.transform;

import com.google.common.collect.Sets;
import com.google.common.math.LongMath;
import com.zhytnik.algo.math.Expression;
import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class Combinations implements GroupTransformation<Set<Expression>, Set<Expression>> {

    private final int expectedSize;

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public Collection<Set<Expression>> apply(Set<Expression> source) {
        return source.size() > expectedSize ? Sets.combinations(source, expectedSize) : List.of(source);
    }

    @Override
    public long expectedCount(int size) {
        return LongMath.binomial(size, expectedSize);
    }
}
