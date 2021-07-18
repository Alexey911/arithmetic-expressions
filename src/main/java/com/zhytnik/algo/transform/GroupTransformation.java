package com.zhytnik.algo.transform;

import com.zhytnik.algo.math.Expression;

import java.util.Collection;

public interface GroupTransformation<S extends Collection<? extends Expression>, T extends Collection<Expression>> {

    Collection<T> apply(S source);

    long expectedCount(int size);
}
