package io.cloudslang.tools.generator.entities.cs;

import io.cloudslang.tools.generator.entities.annotations.Ignore;
import io.cloudslang.tools.generator.entities.annotations.Property;

/**
 * Author: Ligia Centea
 * Date: 4/3/2016.
 */
public class CsInput {

    @Ignore
    private String name;

    private boolean required;

    @Property(key = "default")
    private String defaultValue;

    @Property(key = "private")
    private boolean privateField;

    @Property(key = "sensitive")
    private boolean sensitive;

    public CsInput(String name, boolean required, String defaultValue, boolean sensitive, boolean overridable) {
        this.name = name;
        this.required = required;
        this.defaultValue = defaultValue;
        this.privateField = overridable;
        this.sensitive = sensitive;
    }

    public CsInput(String name, boolean required) {
        this(name, required, null, false, false);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isPrivateField() {
        return privateField;
    }

    public void setPrivateField(boolean privateField) {
        this.privateField = privateField;
    }

    public boolean isSensitive() {
        return sensitive;
    }

    public void setSensitive(boolean sensitive) {
        this.sensitive = sensitive;
    }
}
