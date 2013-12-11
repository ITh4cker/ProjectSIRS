#if defined(WINDOWS) || defined(__WIN32__) || defined(_WIN32) || defined(__CYGWIN32__)
#define popen _popen
#define pclose _pclose
#include <windows.h>
#endif

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "generated/com_sirs_scanner_commands_HardcoreScan.h"

#define BUFF_SIZE 40

JNIEXPORT jstring JNICALL Java_com_sirs_scanner_commands_HardcoreScan_getAllProcessesCPU (JNIEnv *env, jobject obj) {
	FILE *fp;
	int status;
	char *out = NULL;
	int out_size = 0;
	char buff[BUFF_SIZE];

	/* Open the command for reading. */
	fp = popen("ps -eo \"%p %C\" 2> /dev/null", "r");
	if (fp == NULL) {
		fprintf(stderr, "Failed to run command\n" );
		return NULL;
	}

	/* Read the output a line at a time - output it. */
	while (fgets(buff, BUFF_SIZE, fp) != NULL) {
		int read = strlen(buff);
		out_size += read;
		out = (char *) realloc(out, out_size*sizeof(char)+1);
		out[out_size-read] = '\0';
		strcat(out, buff);
	}

	/* close */
	pclose(fp);

	jstring jstrBuf = (*env)->NewStringUTF(env, out);
	free(out);
	return jstrBuf;
}

JNIEXPORT jstring JNICALL Java_com_sirs_scanner_commands_HardcoreScan_getWebcamUsage (JNIEnv *env , jobject obj){
	FILE *fp;
	int status;
	char *out = NULL;
	int out_size = 0;
	char buff[BUFF_SIZE];

	/* Open the command for reading. */
	fp = popen("lsof -F p /dev/video* 2> /dev/null", "r");
	if (fp == NULL) {
		fprintf(stderr, "Failed to run command\n" );
		return NULL;
	}

	/* Read the output a line at a time - output it. */
	while (fgets(buff, BUFF_SIZE, fp) != NULL) {
		int read = strlen(buff);
		out_size += read;
		out = (char *) realloc(out, out_size*sizeof(char)+1);
		out[out_size-read] = '\0';
		strcat(out, buff);
	}

	/* close */
	pclose(fp);

	jstring jstrBuf = (*env)->NewStringUTF(env, out);
	free(out);
	return jstrBuf;
}
