package com.sirs.analyser;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.sirs.scanner.Container;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.Logistic;

public class Analyser {

	private Classifier classifier = new Logistic();
	private final String LOGISTIC_MODEL_NAME;
	
	public Analyser() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL url = classLoader.getResource("logistic.model");
		LOGISTIC_MODEL_NAME = url.getFile();
	}
	
	private List<Integer> parseResult(String result) {
		LineNumberReader reader = new LineNumberReader(new StringReader(result));
		String instance ="";
		List<Integer> instancesWithVirus = new ArrayList<Integer>();
		try {
			reader.readLine();
			reader.readLine();
			reader.readLine();
			reader.readLine();
			reader.readLine();
			
			instance = reader.readLine();
			while(instance != null) {
				String[] instanceValues = instance.split("\\s+");
//				System.out.println("Tamanho da linha de uma instancia "+ instanceValues.length);
				String predicted = instanceValues[3].split(":")[1];
//				System.out.println("predicted = " + predicted);
				if(predicted.equals("true")) {
					instancesWithVirus.add(Integer.parseInt(instanceValues[1]));
				}
				instance = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return instancesWithVirus;
	}
	
	private void printProcessesWithVirus(List<Integer> instancesWithVirus,List<Container> processes) {
		for (Integer instanceId : instancesWithVirus) {
			Container container = processes.get(instanceId-1);
			System.out.println("Virus detected with " + "Process PID = " + container.getExtraInfo("Pid") + " Process Name = " + container.getExtraInfo("Name"));
		}
	}
	
	public void process(String testFileName, List<Container> processes) {
		
		try {
			String result = Evaluation.evaluateModel(classifier,new String[]{"-T",testFileName,"-l",LOGISTIC_MODEL_NAME, "-p","0"});
			List<Integer> instancesWithVirus = parseResult(result);
			printProcessesWithVirus(instancesWithVirus, processes);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
}
