package com.sirs.scanner;

import java.io.Serializable;

public class Resource implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 7862269510472568714L;
    private String type;
    private String value;

    private Resource(String type) {
        this.type = type;
    }

    public Resource(String type, boolean value) {
        this(type);
        this.value = Boolean.toString(value);
    }

    public Resource(String type, char value) {
        this(type);
        this.value = Character.toString(value);
    }

    public Resource(String type, double value) {
        this(type);
        this.value = Double.toString(value);
    }

    public Resource(String type, float value) {
        this(type);
        this.value = Float.toString(value);
    }

    public Resource(String type, int value) {
        this(type);
        this.value = Integer.toString(value);
    }

    public Resource(String type, long value) {
        this(type);
        this.value = Long.toString(value);
    }

    public Resource(String type, String value) {
        this(type);
        this.value = value;
    }

    public String getType() {
        return this.type;
    }

    public String getValue() {
        return this.value;
    }
}
