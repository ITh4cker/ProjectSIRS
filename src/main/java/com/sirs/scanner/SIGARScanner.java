package com.sirs.scanner;

import java.util.ArrayList;
import java.util.List;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.SigarProxyCache;

import com.sirs.scanner.sigar.commands.MyTop;

public class SIGARScanner extends SingleScanner {
    private static final int SLEEP_TIME = 1000 * 5;
    private final SigarProxy sigar;

    public SIGARScanner() {
        this.sigar = SigarProxyCache.newInstance(new Sigar(), SLEEP_TIME);
    }

    @Override
    public List<Container> scan() {

        List<Container> result = new ArrayList<Container>();
        result.addAll(MyTop.execute(this.sigar));

        return result;
    }
}
