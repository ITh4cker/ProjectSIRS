package com.sirs.analyser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import com.sirs.scanner.Container;
import com.sirs.scanner.communication.ARFFPrinter;

public class Listener {

    private final static int SERVER_SOCKET_PORT = 12000;
    private final static String TEST_FILE_NAME = ".testfile.arff";
    private ServerSocket serverSocket;
    private Analyser analyser = null;

    public Listener() {
        try {
            this.serverSocket = new ServerSocket(SERVER_SOCKET_PORT);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void registerAnalyser(Analyser analyser) {
        this.analyser = analyser;
    }

    @SuppressWarnings("unchecked")
    public void run() {

        try {
            Socket socket = this.serverSocket.accept();
            int count = 1;
            while (true) {
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                List<Container> processes = (List<Container>) in.readObject();
                ARFFPrinter printer = new ARFFPrinter(false);

                System.out.println("#" + count + "  Analysing " + processes.size() + " instances");
                String outputString = "";
                for (Container container : processes) {
                    outputString += container.accept(printer);
                }
                writeToFile(outputString);
                this.analyser.process(TEST_FILE_NAME, processes);
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void writeToFile(String output) {
        try {
            FileWriter file = new FileWriter(new File(TEST_FILE_NAME));
            file.write(output);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
