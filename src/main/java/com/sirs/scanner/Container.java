package com.sirs.scanner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Container implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 331462851695387988L;
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

    public String accept(Printer p) {
        return p.print(this);
    }

    public void addResource(Resource r) {
        this.resources.add(r);
    }

    public String getName() {
        return this.name;
    }

    public List<Resource> getResources() {
        return this.resources;
    }
}
