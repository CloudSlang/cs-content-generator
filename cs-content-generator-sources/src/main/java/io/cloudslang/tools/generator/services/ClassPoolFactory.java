package io.cloudslang.tools.generator.services;


import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import javassist.ClassClassPath;
import javassist.ClassPool;
import org.springframework.beans.factory.FactoryBean;

/**
 * Author: Ligia Centea
 * Date: 5/18/2016.
 */
public class ClassPoolFactory implements FactoryBean<ClassPool> {

    @Override
    public ClassPool getObject() throws Exception {
        ClassPool classPool = ClassPool.getDefault();
        classPool.appendClassPath(new ClassClassPath(Action.class));
        classPool.appendClassPath(new ClassClassPath(Param.class));
        classPool.appendClassPath(new ClassClassPath(Output.class));
        classPool.appendClassPath(new ClassClassPath(Response.class));
        return classPool;
    }

    @Override
    public Class<?> getObjectType() {
        return ClassPool.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
