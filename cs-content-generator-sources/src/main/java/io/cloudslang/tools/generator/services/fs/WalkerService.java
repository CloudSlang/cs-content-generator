package io.cloudslang.tools.generator.services.fs;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import static org.springframework.util.Assert.notNull;

/**
 * User: centea
 * Date: 11/10/2015
 */
@Service("walkerService")
public class WalkerService {

    public Iterator<Path> walk(Path root, String[] extsArray) throws IOException {
        notNull(root);
        notNull(extsArray);

        List<String> extensionList = Arrays.asList(extsArray);
        Predicate<? super Path> extensionPredicate = p -> !Files.isDirectory(p) && Files.isRegularFile(p) && extensionList.contains(FilenameUtils.getExtension(p.toString()));

        return Files.walk(root).filter(extensionPredicate).iterator();
    }
}
