package com.sirs.ml;

import static weka.core.converters.ConverterUtils.DataSource;

import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;

import com.sirs.ml.io.ConverterUtils;

public class NaiveBayesFunction implements Function {
    private NaiveBayes algorithm;

    public NaiveBayesFunction(String trainFile) throws Exception {
        DataSource trainData = new DataSource(trainFile);
        algorithm = new NaiveBayes();
        algorithm.buildClassifier(trainData.getDataSet());
    }

    @Override
    public double apply(final Variable[] sample) throws Exception {
        String testFile = ConverterUtils.createTrainFile(sample);
        DataSource testData = new DataSource(testFile);
        Instances testInstances = testData.getDataSet();

        assert testInstances.numInstances() == 1 : "Invalid number of test data!";
        return algorithm.classifyInstance(testInstances.instance(0));
    }
}
