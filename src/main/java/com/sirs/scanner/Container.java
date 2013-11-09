package com.sirs.scanner;

import java.util.ArrayList;
import java.util.List;

public class Container {
    private List<Resource> resources = new ArrayList<Resource>();
    private String name;

    public Container(String name) {
        this.name = name;
    }

    public Container(String name, List<Resource> resources) {
        this.name = name;
        this.resources.addAll(resources);
    }

    public Container(String name, Resource... resources) {
        this.name = name;
        for (Resource resource : resources) {
            this.resources.add(resource);
        }
    }

    public List<Resource> getResources() {
        return this.resources;
    }

    public String getName() {
        return name;
    }

    public void addResource(Resource r) {
        this.resources.add(r);
    }

    public String accept(Printer p) {
        return p.print(this);
    }
}
