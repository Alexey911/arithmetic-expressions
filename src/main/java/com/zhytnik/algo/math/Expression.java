package com.zhytnik.algo.math;

import java.util.List;

public interface Expression {

    double value();

    List<Variable> variables();

    String writtenFormat();

    boolean isUnary();
}
