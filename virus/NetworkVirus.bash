#!/bin/bash

# udp server
nc -ul 9999 &

# udp client
javac NetworkClient.java
java NetworkClient 9999
