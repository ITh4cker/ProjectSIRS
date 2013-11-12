package com.sirs.ml.io;

import java.io.*;

import com.sirs.ml.Function;
import com.sirs.ml.Variable;

public class ConverterUtils {
    private static final String TRAIN_DATA_FILE = "train-data.tmp";

    /**
     * Creates a CSV file with the data needed to apply a function
     * 
     * @param data the data to be used by the function
     * 
     * @return a file with the data
     */
    public static String createTrainFile(Variable[] data) throws IOException {
        PrintWriter printer = new PrintWriter(new BufferedWriter(new FileWriter(TRAIN_DATA_FILE)));

        for (Variable v : data) {
            printer.print(v.getValue());
            printer.print(';');
        }

        return TRAIN_DATA_FILE;
    }

    public static void saveFunctionToFile(Function f, String file) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
        out.writeObject(f);
        out.close();
    }
}
