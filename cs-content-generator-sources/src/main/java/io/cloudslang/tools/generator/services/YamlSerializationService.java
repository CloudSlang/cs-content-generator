package io.cloudslang.tools.generator.services;

import io.cloudslang.tools.generator.entities.dto.FlowFileDTO;
import io.cloudslang.tools.generator.entities.dto.OperationFileDTO;
import java.beans.IntrospectionException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import static org.yaml.snakeyaml.DumperOptions.ScalarStyle.*;


/**
 * Author: Ligia Centea
 * Date: 4/3/2016.
 */
@Service
public class YamlSerializationService {

    public <T> String serialize(T object, Path result) throws IOException {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setDefaultScalarStyle(PLAIN);

        Representer representer = new Representer();
        representer.addClassTag(OperationFileDTO.class, Tag.MAP);
        representer.addClassTag(FlowFileDTO.class, Tag.MAP);
        representer.setPropertyUtils(new UnsortedPropertyUtils());

        Yaml yaml = new Yaml(representer, options);
        return yaml.dump(object);
    }

    private class UnsortedPropertyUtils extends PropertyUtils {
        @Override
        protected Set<Property> createPropertySet(Class<?> type, BeanAccess bAccess)
                throws IntrospectionException {
            return new LinkedHashSet<>(getPropertiesMap(type, BeanAccess.FIELD).values());
        }
    }


}
