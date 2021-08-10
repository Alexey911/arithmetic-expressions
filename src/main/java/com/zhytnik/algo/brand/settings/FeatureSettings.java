package com.zhytnik.algo.brand.settings;

import com.zhytnik.algo.brand.filter.ast.AstNormalization;
import com.zhytnik.algo.brand.filter.ast.DefaultNodeComparator;
import com.zhytnik.algo.brand.filter.ast.Node;
import com.zhytnik.algo.brand.threshold.AbsoluteErrorThreshold;
import com.zhytnik.algo.brand.threshold.Threshold;

import java.util.Comparator;
import java.util.Objects;

public final class FeatureSettings {

    private boolean useSameValues;
    private boolean groupElimination;
    private Threshold similarityThreshold;
    private AstNormalization normalization;
    private Comparator<Node> nodeComparator;

    public FeatureSettings() {
        useSameValues = false;
        groupElimination = true;
        nodeComparator = DefaultNodeComparator.INSTANCE;
        similarityThreshold = new AbsoluteErrorThreshold(0.000001d);
        normalization = new AstNormalization(nodeComparator);
    }

    public boolean useSameValues() {
        return useSameValues;
    }

    public boolean useGroupElimination() {
        return groupElimination;
    }

    public Threshold similarityThreshold() {
        return similarityThreshold;
    }

    public AstNormalization normalization() {
        return normalization;
    }

    public Comparator<Node> nodeComparator() {
        return nodeComparator;
    }

    public FeatureSettings useGroupElimination(boolean groupElimination) {
        this.groupElimination = groupElimination;
        return this;
    }

    public FeatureSettings applySimilarityThreshold(Threshold similarityThreshold) {
        this.similarityThreshold = Objects.requireNonNull(similarityThreshold);
        return this;
    }

    public FeatureSettings applyNormalization(AstNormalization normalization) {
        this.normalization = Objects.requireNonNull(normalization);
        return this;
    }

    public FeatureSettings applyNodeComparator(Comparator<Node> nodeComparator) {
        this.nodeComparator = Objects.requireNonNull(nodeComparator);
        return this;
    }

    public FeatureSettings useSameValues(boolean useSameValues) {
        this.useSameValues = useSameValues;
        return this;
    }

    public static FeatureSettings defaultSettings() {
        return new FeatureSettings();
    }
}
