package com.sirs.scanner;

public abstract class Printer {
    public abstract String print(Container c);

    public String printHeader(Container c) {
        return "";
    }
}
