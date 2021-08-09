package com.zhytnik.algo.brand.input;

import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.zhytnik.algo.brand.data.Variable;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsonDataSource implements VariableDataSource {

    @Override
    @SneakyThrows
    public Set<Variable> variables(InputStream resource) {
        ObjectMapper objectMapper = JsonMapper.builder()
                .enable(JsonReadFeature.ALLOW_NON_NUMERIC_NUMBERS)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();

        var source = objectMapper.readValue(resource, Map.class);

        Map<String, Variable> target = new LinkedHashMap<>();

        parseMap(source, "", target);

        return new LinkedHashSet<>(target.values());
    }

    private void parseMap(Map<?, ?> value, String path, Map<String, Variable> target) {
        for (var e : value.entrySet()) {
            parseObject(e.getValue(), toPath(path, e.getKey().toString()), target);
        }
    }

    private void parseObject(Object value, String path, Map<String, Variable> target) {
        if (value instanceof Number) {
            double d = ((Number) value).doubleValue();
            if (!Double.isNaN(d) && d != 0d) {
                target.put(path, new Variable(path, d));
            }
        } else if (value instanceof Map) {
            parseMap((Map<?, ?>) value, path, target);
        } else if (value instanceof List) {
            var list = (List<?>) value;

            for (int i = 0; i < list.size(); i++) {
                parseObject(list.get(0), toPath(path, Integer.toString(i)), target);
            }
        }
    }

    private String toPath(String base, String add) {
        return base.isEmpty() ? add : base + "." + add;
    }
}
