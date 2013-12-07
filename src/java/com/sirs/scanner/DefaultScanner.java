package com.sirs.scanner;

import java.util.ArrayList;
import java.util.List;

import com.sirs.scanner.commands.HardcoreScan;

public class DefaultScanner extends SingleScanner {

    @Override
    public List<Container> scan() {
        List<Container> lst = new ArrayList<Container>();
        String out = (new HardcoreScan()).getAllProcessesCPU();
        System.out.println(out);
        return lst;
    }

}
