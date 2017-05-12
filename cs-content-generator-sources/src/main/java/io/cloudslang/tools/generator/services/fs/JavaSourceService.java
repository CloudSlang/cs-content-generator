package io.cloudslang.tools.generator.services.fs;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import io.cloudslang.tools.generator.utils.NameUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Ligia Centea
 * Date: 3/15/2016.
 */
@Service("javaSourceService")
public class JavaSourceService {

    public CompilationUnit getCompilationUnit(File src) throws IOException, ParseException {
        return JavaParser.parse(src);
    }

    public List<TypeDeclaration> getDeclaredTypes(CompilationUnit javaClass) {
        Assert.notNull(javaClass);
        List<TypeDeclaration> types = javaClass.getTypes();
        if (types == null) {
            return new ArrayList<>();
        }
        return types;
    }

    public List<BodyDeclaration> getTypeMembers(TypeDeclaration typeDeclaration) {
        Assert.notNull(typeDeclaration);
        List<BodyDeclaration> members = typeDeclaration.getMembers();
        if (members != null) {
            return members;
        }
        return new ArrayList<>();
    }

    public String getPackage(TypeDeclaration typeDeclaration) {
        Assert.notNull(typeDeclaration);
        if (typeDeclaration.getParentNode() instanceof CompilationUnit) {
            CompilationUnit javaClass = (CompilationUnit) typeDeclaration.getParentNode();
            return javaClass.getPackage().getName().toString();
        }
        return null;
    }

    public String getFullyQualifiedName(TypeDeclaration typeDeclaration) {
        String packageName = getPackage(typeDeclaration);
        return NameUtils.getFullyQualifiedName(packageName, typeDeclaration.getName());
    }

    public boolean isImplementation(TypeDeclaration type, String iAction) {
        if (type instanceof ClassOrInterfaceDeclaration) {
            ClassOrInterfaceDeclaration classDeclaration = (ClassOrInterfaceDeclaration) type;
            List<ClassOrInterfaceType> interfaces = classDeclaration.getImplements();
            if (interfaces != null) {
                return interfaces.stream().anyMatch(i -> i.getName().equals(iAction));
            }
        }
        return false;
    }
}
