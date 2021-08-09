package com.zhytnik.algo.brand.data;

import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;

public final class Variable implements Expression {

    private final String name;
    private final double value;

    public Variable(String name, double value) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("Blank names are illegal! Actual is " + name);
        }
        this.name = name;
        this.value = value;
    }

    @Override
    public double value() {
        return value;
    }

    @Override
    public List<Variable> variables() {
        return singletonList(this);
    }

    @Override
    public Expression recalculateWith(Map<? extends Expression, ? extends Expression> replacements) {
        return this;
    }

    @Override
    public String formatted() {
        return name;
    }

    @Override
    public boolean isUnary() {
        return true;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object another) {
        if (this == another) {
            return true;
        }
        if (another == null || getClass() != another.getClass()) {
            return false;
        }
        return name.equals(((Variable) another).name);
    }
}
