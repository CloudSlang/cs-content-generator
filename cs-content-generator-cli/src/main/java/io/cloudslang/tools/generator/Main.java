package io.cloudslang.tools.generator;

import io.cloudslang.tools.generator.services.MigrationService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Author: Ligia Centea
 * Date: 4/5/2016.
 */
public class Main {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("tool-spring-context.xml");
        MigrationService migrationService = (MigrationService)context.getBean("migrationService");
        migrationService.migrate(args);
    }
}
