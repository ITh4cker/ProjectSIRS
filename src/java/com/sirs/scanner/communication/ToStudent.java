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

    public ToStudent(String fileName, Printer p) throws IOException {
        this.file = new FileWriter(new File(fileName));
        this.printer = p;
    }

    @Override
    public boolean send(List<Container> c) {
        try {
            for (Container container : c) {
                this.file.write(container.accept(this.printer));
            }
            this.file.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

	@Override
	public void close() {
		try {
			file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
