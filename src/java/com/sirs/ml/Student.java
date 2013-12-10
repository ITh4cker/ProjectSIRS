package com.sirs.ml;

import java.io.IOException;

import weka.classifiers.functions.Logistic;

import com.sirs.ml.io.ConverterUtils;

public class Student {

    /**
     * Creates a function, trains it and saves it to a given file
     * 
     * args[0] = train data file
     * args[1] = function output file
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        assert args.length == 2;

        String trainFile = args[0];
        String functionFile = args[1];
        Logistic.main(new String[] {"-T", "test-file.arff", "-l", "logistic.model", "-p", "0"});
        
        try {
            NaiveBayesFunction f = new NaiveBayesFunction(trainFile);
            ConverterUtils.saveFunctionToFile(f, functionFile);
        } catch (IOException e) {
            System.err.println("Can't save function to file " + functionFile);
        } catch (Exception e) {
            System.err.println("Error while creating the function");
        }
    }
}
