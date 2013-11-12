package com.sirs.scanner;

public class Resource {
    private String type;
    private String value;

    public Resource(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
