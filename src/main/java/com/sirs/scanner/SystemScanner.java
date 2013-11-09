package com.sirs.scanner;

import java.io.IOException;
import java.util.List;

import com.sirs.scanner.communication.Sender;
import com.sirs.scanner.communication.ToAnalyser;
import com.sirs.scanner.communication.ToStudent;

public class SystemScanner {
    private static final String STUDENT_MODE = "STUDENT";
    private static final String ANALYSER_MODE = "ANALYSER";
    private static final int WAIT_TIME_BETWEEN_SCANS = 5000; //Time in milliseconds.
    private static int timesToScan = -1; //negative values means infinite times to scan

    private static ScannerManager scanner = null;
    private static Sender sender = null;

    public static void main(String[] args) {
        System.out.println("Starting Scanner...");
        try {
            init(args);
        } catch (IOException e1) {
            e1.printStackTrace();
            return;
        }
        System.out.println("Scanner initialized");
        for (int times = 0; times != timesToScan; times++) {
            System.out.print("Scan #" + times + ": ");
            final List<Container> processes = getSystemActivity();
            System.out.println("scanned " + processes.size() + " elements");
            sender.send(processes);

            try {
                Thread.sleep(WAIT_TIME_BETWEEN_SCANS);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
        System.out.println("Scanner terminated");
    }

    private static List<Container> getSystemActivity() {
        return scanner.scan();
    }

    private static void init(String[] args) throws IOException {
        boolean correctArgs = false;
        for (String string : args) {
            if (string.equals(ANALYSER_MODE)) {
                timesToScan = -1;
                correctArgs = true;
                sender = new ToAnalyser();
            } else if (string.equals(STUDENT_MODE)) {
                timesToScan = 1;
                correctArgs = true;
                sender = new ToStudent("test-file.txt");
            }
        }
        if (!correctArgs) {
            throw new RuntimeException("Invalid Arguments");
        }

        scanner = new ScannerManager(new DefaultScanner(), new SIGARScanner());
    }

}
