package com.zhytnik.algo.brand.filter.ast;

import java.util.function.UnaryOperator;

public final class AstNormalization implements UnaryOperator<Tree> {

    private final UnaryOperator<Tree>[] chain;

    public AstNormalization() {
        this(
                new BinaryGrouping(),
                new SubtractionSeparation(),
                new DivisionSimplification(),
                new SignSimplification(),
                new GroupAndSort()
        );
    }

    @SafeVarargs
    public AstNormalization(UnaryOperator<Tree>... operators) {
        chain = operators;
    }

    @Override
    public Tree apply(Tree tree) {
        for (var op : chain) {
            op.apply(tree);
        }
        return tree;
    }
}
