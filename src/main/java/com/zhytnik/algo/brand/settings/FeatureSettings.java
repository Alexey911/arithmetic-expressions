package com.zhytnik.algo.brand.settings;

import com.zhytnik.algo.brand.filter.ast.AstNormalization;
import com.zhytnik.algo.brand.threshold.AbsoluteErrorThreshold;
import com.zhytnik.algo.brand.threshold.Threshold;

import java.util.Objects;

public final class FeatureSettings {

    private boolean groupElimination;
    private Threshold similarityThreshold;
    private AstNormalization normalization;

    public FeatureSettings() {
        groupElimination = true;
        normalization = new AstNormalization();
        similarityThreshold = new AbsoluteErrorThreshold(0.000001d);
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

    public static FeatureSettings defaultSettings() {
        return new FeatureSettings();
    }
}
