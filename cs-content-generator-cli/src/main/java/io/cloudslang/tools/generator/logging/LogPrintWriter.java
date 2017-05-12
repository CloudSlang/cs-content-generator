package io.cloudslang.tools.generator.logging;

import java.io.PrintWriter;
import org.apache.commons.io.output.NullWriter;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * Author: Ligia Centea
 * Date: 4/28/2016.
 */
public class LogPrintWriter extends PrintWriter {

    private Logger logger;
    private Marker marker = MarkerFactory.getMarker("");

    public LogPrintWriter(Logger logger) {
        super(new NullWriter());
        this.logger = logger;
    }

    @Override
    public void println(String sb) {
        if (logger != null) {
            logger.info(marker, sb);
        }
    }
}
