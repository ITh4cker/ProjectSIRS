package com.sirs.scanner.communication;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.sirs.scanner.Container;
import com.sirs.scanner.Printer;

public class ToStudent extends Sender {
    private final FileWriter file;
    private final Printer printer;

    @Deprecated
    public ToStudent(String fileName) throws IOException {
        this(fileName, new CSVPrinter());
    }

    public ToStudent(String fileName, Printer p) throws IOException {
        this.file = new FileWriter(new File(fileName));
        this.printer = p;
    }

    @Override
    public boolean send(List<Container> c) {
        try {
            if (!c.isEmpty()) {
                this.file.write(this.printer.printHeader(c.get(0)));
            }
            for (Container container : c) {
                this.file.write(container.accept(this.printer));
            }
            this.file.flush();
            this.file.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
