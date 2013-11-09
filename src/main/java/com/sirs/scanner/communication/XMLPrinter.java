package com.sirs.scanner.communication;

import com.sirs.scanner.Container;
import com.sirs.scanner.Printer;
import com.sirs.scanner.Resource;

public class XMLPrinter extends Printer {

    @Override
    public String print(Container c) {
        String result = "<container name=" + c.getName() + " >\n";
        for (Resource res : c.getResources()) {
            result += "\t<resource type=" + res.getType() + " >" + res.getValue() + "</resource>\n";
        }
        result += "</container>\n";
        return result;
    }

}
