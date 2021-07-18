package com.zhytnik.algo.data;

import com.zhytnik.algo.math.Variable;

import java.io.InputStream;
import java.util.Set;

public interface VariableSearch {

    Set<Variable> search(InputStream resource);
}
