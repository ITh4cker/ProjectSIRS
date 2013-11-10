package com.sirs.scanner.sigar.commands;

import java.util.ArrayList;
import java.util.List;

import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;

import com.sirs.scanner.Container;

public class MyNetstat {

    public static List<Container> execute(final SigarProxy sigar) {
        List<Container> result = new ArrayList<Container>();
        getPids(sigar);
        return result;
    }

    private static long[] getPids(final SigarProxy sigar) {
        try {
            return sigar.getProcList();
        } catch (SigarException e) {
            e.printStackTrace();
            return new long[0];
        }
    }
}
