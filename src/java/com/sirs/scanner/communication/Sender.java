package com.sirs.scanner.communication;

import java.util.List;

import com.sirs.scanner.Container;

public abstract class Sender {
    public abstract boolean send(List<Container> c);
}
