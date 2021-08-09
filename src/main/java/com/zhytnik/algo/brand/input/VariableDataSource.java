package com.zhytnik.algo.brand.input;

import com.zhytnik.algo.brand.data.Variable;

import java.io.InputStream;
import java.util.Set;

public interface VariableDataSource {

    Set<Variable> variables(InputStream resource);
}
