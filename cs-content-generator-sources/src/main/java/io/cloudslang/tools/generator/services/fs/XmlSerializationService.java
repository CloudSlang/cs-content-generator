package io.cloudslang.tools.generator.services.fs;

import com.thoughtworks.xstream.XStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.file.Path;

/**
 * Author: Ligia Centea
 * Date: 3/15/2016.
 */
@Service("xmlSerializationService")
public class XmlSerializationService {

    @Autowired
    private XStream xStream;

    @PostConstruct
    public void init() {
        xStream.ignoreUnknownElements();
    }

    public <T> T deserialize(String s, Class<T> type) {
        xStream.processAnnotations(type);
        return type.cast(xStream.fromXML(s));
    }

    public <T> T deserialize(Path path, Class<T> type) {
        xStream.processAnnotations(type);
        return type.cast(xStream.fromXML(path.toFile()));
    }
}
