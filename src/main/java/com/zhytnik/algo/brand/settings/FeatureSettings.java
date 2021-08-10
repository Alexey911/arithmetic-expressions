package com.zhytnik.algo.brand.settings;

import com.zhytnik.algo.brand.threshold.AbsoluteErrorThreshold;
import com.zhytnik.algo.brand.threshold.Threshold;

public class FeatureSettings {

    private boolean groupElimination;
    private Threshold similarityThreshold;

    public FeatureSettings() {
        groupElimination = true;
        similarityThreshold = new AbsoluteErrorThreshold(0.000001d);
    }

    public boolean useGroupElimination() {
        return groupElimination;
    }

    public Threshold similarityThreshold() {
        return similarityThreshold;
    }

    public FeatureSettings useGroupElimination(boolean groupElimination) {
        this.groupElimination = groupElimination;
        return this;
    }

    public FeatureSettings applySimilarityThreshold(Threshold similarityThreshold) {
        this.similarityThreshold = similarityThreshold;
        return this;
    }

    public static FeatureSettings defaultSettings() {
        return new FeatureSettings();
    }
}
