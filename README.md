ProjectSIRS
===========

##### To run the project you have to:

1. Create a JAVA_HOME environment variable
example: export JAVA_HOME='/usr/lib/jvm/oracle-jdk-7' 

2. Train the learning algorithm with the already created arff file
ant create-model

3. Run the analyser and the scanner, in this order
ant run-analyser
ant run-scanner
