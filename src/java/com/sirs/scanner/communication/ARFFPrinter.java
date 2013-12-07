package com.sirs.scanner.communication;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sirs.scanner.Container;
import com.sirs.scanner.Printer;
import com.sirs.scanner.Resource;

public class ARFFPrinter extends Printer {
    private boolean header_init = true;
    private final boolean learningMode;
    private final List<String> pinnedAsVirus;

    public ARFFPrinter(boolean learningMode) {
        this.learningMode = learningMode;
        this.pinnedAsVirus = new ArrayList<String>();
    }

    public ARFFPrinter(List<String> listOfVirus) {
        this.learningMode = true;
        this.pinnedAsVirus = listOfVirus;
    }

    public ARFFPrinter(String... virus) {
        List<String> listOfVirus = new ArrayList<String>();
        for (String string : virus) {
            listOfVirus.add(string);
        }
        this.learningMode = true;
        this.pinnedAsVirus = listOfVirus;
    }

    private String init(Container c) {
        this.header_init = false;
        String header = "";
        header += "% Auto-generated file for SIRS Project\n";
        header += "% Date: " + (new Date()).toString() + "\n";
        header += "%\n";
        header += "@RELATION virus\n\n";
        for (Resource resource : c.getResources()) {
            header += String.format("@ATTRIBUTE %s %s\n", resource.getHeader(), resource.getValueType());
        }

        if (isLearningMode()) {
            header += "@ATTRIBUTE class {true,false}\n";
        }
        header += "\n@DATA\n";

        return header;
    }

    public boolean isLearningMode() {
        return this.learningMode;
    }

    private boolean isVirus(Container c) {
        for (String pinned : this.pinnedAsVirus) {
            if (c.getExtraInfo("Name").equals(pinned)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String print(Container c) {
        String slot = "";
        if (this.header_init) {
            //adding header
            slot = init(c);
        }
        for (Resource resource : c.getResources()) {
            slot += resource.getValue() + ",";
        }
        if (isLearningMode()) {
            slot += isVirus(c);
        } else {
            slot = slot.substring(0, slot.length() - 1); //remove last comma
        }
        return slot + "\n";
    }
}
