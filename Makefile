JCLASS = com.sirs.scanner.commands.HardcoreScan
CFILE = HardcoreScanImp
SDIR = src/C
GDIR = $(SDIR)/generated
ODIR = build
LDIR = lib
LOUT = libsystemscan.so
CC = gcc
INC = -I/usr/local/java/include -I/usr/local/java/include/genunix -I$(JAVA_HOME)/include/ -I$(JAVA_HOME)/include/linux -fPIC

all: header compile library

header:
	javah -cp "$(ODIR)/classes" -d $(GDIR) $(JCLASS)

compile:
	$(CC) $(INC) -c -o $(ODIR)/$(CFILE).o $(SDIR)/$(CFILE).c

library:
	$(CC) -shared -Wl,-soname,$(LOUT).1 -o $(LDIR)/$(LOUT) $(ODIR)/$(CFILE).o

clean:
	rm -rf $(LDIR)/$(LOUT) $(ODIR)/$(CFILE).o
