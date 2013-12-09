package com.sirs.scanner;

import java.io.Serializable;

public class Resource implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 7862269510472568714L;
    private String header;
    private String valueType;
    private String value;

    private Resource(String type) {
        this.header = type;
    }

    public Resource(String type, boolean value) {
        this(type);
        this.value = value ? "1" : "0";
        this.valueType = "NUMERIC";
    }

    public Resource(String type, char value) {
        this(type);
        this.value = Integer.toString((int) value);
        this.valueType = "NUMERIC";
    }

    public Resource(String type, double value) {
        this(type);
        this.value = Double.toString(value);
        this.valueType = "NUMERIC";
    }

    public Resource(String type, float value) {
        this(type);
        this.value = Float.toString(value);
        this.valueType = "NUMERIC";
    }

    public Resource(String type, int value) {
        this(type);
        this.value = Integer.toString(value);
        this.valueType = "NUMERIC";
    }

    public Resource(String type, long value) {
        this(type);
        this.value = Long.toString(value);
        this.valueType = "NUMERIC";
    }

    public Resource(String type, String value) {
        this(type);
        this.value = value;
        this.valueType = "STRING";
    }

    public String getHeader() {
        return this.header;
    }

    public String getValue() {
        return this.value;
    }

    public String getValueType() {
        return this.valueType;
    }
}
