package com.sirs.scanner.communication;

import com.sirs.scanner.Container;
import com.sirs.scanner.Printer;
import com.sirs.scanner.Resource;

public class XMLPrinter extends Printer {

    @Override
    public String print(Container c) {
        String result = "<container";
        result += " pid=\"" + c.getName() + "\"";
        result += " name=\"" + c.getExtraInfo("Name") + "\"";
        result += " desc=\"" + c.getExtraInfo("Description") + "\"";
        result += " user=\"" + c.getExtraInfo("USER") + "\"";
        result += ">\n";

        for (Resource res : c.getResources()) {
            result += "\t<resource type=\"" + res.getHeader() + "\">" + res.getValue() + "</resource>\n";
        }
        result += "</container>\n";
        return result;
    }
}
