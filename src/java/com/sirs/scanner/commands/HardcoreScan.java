package com.sirs.scanner.commands;

public class HardcoreScan {
    static {
        System.loadLibrary("systemscan");
    }

    /**
     * Returns the cpu usage per process.
     * <p>
     * Calling this is equivalent to unix command:<br>
     * &nbsp;<code>ps -eo "%p %C"</code>
     * 
     * @return output of command 'ps'
     */
    public native String getAllProcessesCPU();

    /**
     * Returns the processes that are using webcam.
     * <p>
     * Calling this is equivalent to unix command:<br>
     * &nbsp;<code>lsof -F p /dev/video0</code>
     * 
     * @return output of command 'lsof'
     */
    public native String getWebcamUsage();
}
