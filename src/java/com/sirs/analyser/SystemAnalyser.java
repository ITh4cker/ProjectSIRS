package com.sirs.analyser;

public class SystemAnalyser {

	public static void main(String[] args) {
		Listener listener = new Listener();
		Analyser analyser = new Analyser();
		listener.registerAnalyser(analyser);
		listener.run();
	}
}
