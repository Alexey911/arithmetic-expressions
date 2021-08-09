package com.zhytnik.algo.brand.data;

import java.util.List;
import java.util.Map;

public interface Expression {

    double value();

    boolean isUnary();

    String formatted();

    List<Variable> variables();

    Expression recalculateWith(Map<? extends Expression, ? extends Expression> replacements);
}
