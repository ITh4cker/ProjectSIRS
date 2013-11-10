package com.sirs.scanner.communication;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.sirs.scanner.Container;

public class ToStudent extends Sender {
    private FileWriter file = null;

    public ToStudent(String fileName) throws IOException {
        this.file = new FileWriter(new File(fileName));
    }

    @Override
    public boolean send(List<Container> c) {
        try {
            for (Container container : c) {
                this.file.write(container.accept(new XMLPrinter()));
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
