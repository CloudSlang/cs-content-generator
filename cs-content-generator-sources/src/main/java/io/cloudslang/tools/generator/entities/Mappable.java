package io.cloudslang.tools.generator.entities;

import java.util.Map;

public interface Mappable {
    Map<String, Object> toMap();
}
