package com.zhytnik.algo.math;

import java.util.Set;

public final class Variable implements Expression {

    protected final String name;
    protected final double value;

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
    public Set<Variable> variables() {
        return Set.of(this);
    }

    @Override
    public String writtenFormat() {
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
