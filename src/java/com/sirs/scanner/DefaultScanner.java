package com.sirs.scanner;

import java.util.ArrayList;
import java.util.List;

public class DefaultScanner extends SingleScanner {

    @Override
    public List<Container> scan() {
        List<Container> lst = new ArrayList<Container>();
        Container c = new Container("wazaaaaaaaaa.exe");
        c.addResource(new Resource("CPU", "80"));
        c.addResource(new Resource("RAM", "1283M"));
        lst.add(c);
        return lst;
    }

}
