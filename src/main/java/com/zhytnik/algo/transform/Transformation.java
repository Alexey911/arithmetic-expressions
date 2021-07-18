package com.zhytnik.algo.transform;

import com.zhytnik.algo.math.Expression;

import java.util.List;

public interface Transformation {

    long expectedCount(int size);

    List<Expression> apply(List<Expression> source);
}
