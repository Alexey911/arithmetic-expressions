package com.zhytnik.algo.brand.compute;

import com.zhytnik.algo.brand.data.Expression;

import java.util.List;
import java.util.function.UnaryOperator;

public interface Transformation extends UnaryOperator<List<Expression>>, FiniteComputations {

    String shortName();
}
