package com.sirs.scanner.communication;

import com.sirs.scanner.Container;
import com.sirs.scanner.Printer;
import com.sirs.scanner.Resource;

public class CSVPrinter extends Printer {

    @Override
    public String print(Container c) {
        String result = "";
        for (Resource res : c.getResources()) {
            result += res.getValue() + ";";
        }
        result += "\n";
        return result;
    }

    @Override
    public String printHeader(Container c) {
        String result = "";
        for (Resource res : c.getResources()) {
            result += res.getType() + ";";
        }
        result += "\n";
        return result;
    }

}
