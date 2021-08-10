package com.zhytnik.algo.brand.filter;

import com.zhytnik.algo.brand.data.Expression;
import com.zhytnik.algo.brand.threshold.AbsoluteErrorThreshold;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Optional;

import static com.zhytnik.algo.brand.filter.ast.Data.add;
import static com.zhytnik.algo.brand.filter.ast.Data.div;
import static com.zhytnik.algo.brand.filter.ast.Data.multiple;
import static com.zhytnik.algo.brand.filter.ast.Data.sub;
import static org.assertj.core.api.Assertions.assertThat;

class VariableEliminationTest {

    @ParameterizedTest
    @MethodSource("arguments")
    void eliminates(Expression source, boolean valid) {
        var actual = eliminate(source);

        if (valid) {
            assertThat(actual).contains(source);
        } else {
            assertThat(actual).isEmpty();
        }
    }

    Optional<Expression> eliminate(Expression source) {
        var elimination = new VariableElimination(4, new AbsoluteErrorThreshold(0.1));
        var result = elimination.apply(List.of(source));

        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    static List<Arguments> arguments() {
        return List.of(
                Arguments.of(add(1, add(5, add(3, 0.01))), false),
                Arguments.of(add(1, add(5, add(0.01, 3))), false),
                Arguments.of(add(1, add(5, add(1, 3))), true),

                Arguments.of(add(1, add(5, sub(3, 0.01))), false),
                Arguments.of(add(1, add(5, sub(0.01, 3))), false),
                Arguments.of(add(1, add(5, sub(3, 1))), true),

                Arguments.of(multiple(3, add(2, multiple(4, 1.001))), false),
                Arguments.of(multiple(3, add(2, multiple(1.001, 3))), false),
                Arguments.of(multiple(3, add(2, multiple(0.01, 3))), false),
                Arguments.of(multiple(3, add(2, multiple(3, 0.01))), false),
                Arguments.of(multiple(3, add(2, multiple(3, 1.1))), true),

                Arguments.of(add(3, add(2, div(3, 1.001))), false),
                Arguments.of(add(3, add(2, div(0.001, 1000))), false),
                Arguments.of(add(3, add(2, div(3, 1.1))), true)
        );
    }
}
