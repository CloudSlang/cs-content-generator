package io.cloudslang.tools.generator.services.converters;

import io.cloudslang.tools.generator.entities.annotations.Ignore;
import io.cloudslang.tools.generator.entities.annotations.Property;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

import static org.springframework.util.Assert.notNull;

/**
 * Author: Ligia Centea
 * Date: 4/5/2016.
 */
@Service
public class MapConverterService {

    public Map<String, Object> getObjectAsMap(Object object) {
        Map<String, Object> objectMap = new HashMap<>();
        for (Field f : object.getClass().getDeclaredFields()) {
            if (!f.isAnnotationPresent(Ignore.class)) {
                try {
                    boolean isAccessible = f.isAccessible();
                    f.setAccessible(true);
                    Object value;
                    value = f.get(object);
                    f.setAccessible(isAccessible);

                    if (value != null) {
                        if (f.isAnnotationPresent(Property.class)) {
                            Property annotation = f.getAnnotation(Property.class);
                            objectMap.put(annotation.key(), value);
                        } else {
                            objectMap.put(f.getName(), value);
                        }
                    }
                } catch (IllegalAccessException ignored) {
                    //ignore this field, since we cannot access its value
                }
            }
        }
        return objectMap;
    }
}
