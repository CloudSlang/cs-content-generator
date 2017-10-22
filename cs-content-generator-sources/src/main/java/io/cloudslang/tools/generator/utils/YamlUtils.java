package io.cloudslang.tools.generator.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Optional;

/**
 * Created by moldovso on 6/9/2017.
 */
@Slf4j
public class YamlUtils {
    public static Optional<Object> simpleYPath(final Object object, final String yPath, final String delimiter) {
        if (StringUtils.isNotEmpty(yPath)) {
            if (object instanceof Map) {
                final Map map = (Map) object;
                final String whatWeSearchFor = StringUtils.substringBefore(yPath, delimiter);

                final Object something = map.get(whatWeSearchFor);

                if (something != null) {
                    return simpleYPath(something, StringUtils.substringAfter(yPath, delimiter), delimiter);
                }
            }
        } else {
            return Optional.of(object);
        }
        return Optional.empty();
    }
}
