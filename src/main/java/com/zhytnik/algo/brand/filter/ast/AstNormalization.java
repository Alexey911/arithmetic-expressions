package com.zhytnik.algo.brand.filter.ast;

import com.zhytnik.algo.brand.settings.FeatureSettings;

import java.util.Comparator;
import java.util.function.UnaryOperator;

public final class AstNormalization implements UnaryOperator<Tree> {

    private final UnaryOperator<Tree>[] chain;

    public AstNormalization() {
        this(FeatureSettings.defaultSettings());
    }

    public AstNormalization(FeatureSettings settings) {
        this(settings.nodeComparator());
    }

    public AstNormalization(Comparator<Node> nodeComparator) {
        this(
                new SubtractionSeparation(),
                new DivisionSimplification(),
                new SignSimplification(),
                new GroupAndSort(nodeComparator)
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

    public int stepCount() {
        return chain.length;
    }
}
