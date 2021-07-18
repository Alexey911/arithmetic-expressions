package com.zhytnik.algo.math;

import java.util.Set;

public interface Expression {

    double value();

    Set<Variable> variables();

    String writtenFormat();

    boolean isUnary();
}
