package com.zhytnik.algo.brand.compute;

import com.zhytnik.algo.brand.data.Expression;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@AllArgsConstructor
public class LoggingTransformation implements Transformation {

    private final Transformation target;

    @Override
    public long complexity(int sourceSize) {
        return target.complexity(sourceSize);
    }

    @Override
    public String shortName() {
        return target.shortName();
    }

    @Override
    public List<Expression> apply(List<Expression> source) {
        var result = target.apply(source);

        if (result.size() < source.size()) {
            log.debug("{} reduced from {} to {}", target.shortName(), source.size(), result.size());
        }
        return result;
    }
}
