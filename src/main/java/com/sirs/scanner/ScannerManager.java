package com.sirs.scanner;

import java.util.ArrayList;
import java.util.List;

public class ScannerManager implements Scanner {
    private List<SingleScanner> scanners = new ArrayList<SingleScanner>();

    public ScannerManager(SingleScanner... scanners) {
        for (SingleScanner singleScanner : scanners) {
            this.scanners.add(singleScanner);
        }
    }

    public ScannerManager(List<SingleScanner> scanners) {
        this.scanners.addAll(scanners);
    }

    @Override
    public List<Container> scan() {
        List<Container> result = new ArrayList<Container>();
        for (SingleScanner scanner : this.scanners) {
            result.addAll(scanner.scan());
        }
        return result;
    }
}
