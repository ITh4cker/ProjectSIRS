package com.sirs.scanner;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hyperic.sigar.NetConnection;
import org.hyperic.sigar.NetFlags;
import org.hyperic.sigar.ProcUtil;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.SigarProxyCache;

public class SIGARScanner extends SingleScanner {
    private static final int SLEEP_TIME = 1000 * 5;
    private final SigarProxy sigar;
    private final Set<String> knownProcesses;

    @Deprecated
    public SIGARScanner() {
        this("knownProcesses.txt");
    }

    public SIGARScanner(String knownProcessesFile) {
        this.sigar = SigarProxyCache.newInstance(new Sigar(), SLEEP_TIME);
        Set<String> procs = new HashSet<String>();
        try {
            BufferedReader file = new BufferedReader(new FileReader(knownProcessesFile));
            String line;
            while ((line = file.readLine()) != null) {
                procs.add(line);
            }
            file.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.knownProcesses = procs;
    }

    private long[] getPids() {
        try {
            return this.sigar.getProcList();
        } catch (SigarException e) {
            e.printStackTrace();
            return new long[0];
        }
    }

    private long[] getProcessesUsingInternet() {
        try {
            int flags = 0;
            flags |= NetFlags.CONN_TCP | NetFlags.CONN_UDP | NetFlags.CONN_SERVER | NetFlags.CONN_CLIENT;
            NetConnection[] connections = this.sigar.getNetConnectionList(flags);
            List<Long> pids = new ArrayList<Long>();
            for (NetConnection conn : connections) {
                try {
                    long pid = this.sigar.getProcPort(conn.getType(), conn.getLocalPort());
                    if (pid != 0) { //catching bugs on API
                        pids.add(pid);
                    }
                } catch (SigarException e) {
                }
            }
            long[] result = new long[pids.size()];
            int i = 0;
            for (Long l : pids) {
                result[i++] = l;
            }
            return result;
        } catch (SigarException e) {
            e.printStackTrace();
            return new long[0];
        }
    }

    private boolean isKnownProcess(long pid) {
        try {
            return this.knownProcesses.contains(ProcUtil.getDescription(this.sigar, pid));
        } catch (SigarException e) {
            e.printStackTrace();
            return false;
        }
    }

    //TODO I have no idea what to do
    private boolean isListeningToKeyboard(long pid) {
        return false;
    }

    private boolean isRunningAsRoot(long pid) {
        try {
            return this.sigar.getProcCredName(pid).getUser().equals("root");
        } catch (SigarException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isUsingInternet(long pid, long[] pidsUsingInternet) {
        for (long l : pidsUsingInternet) {
            if (l == pid) {
                return true;
            }
        }
        return false;
    }

    //TODO implement this function
    private boolean isUsingWebcam(long pid) {
        return false;
    }

    /* 
     * get all processes running
     * for each process do:
     *      -> get % cpu
     *      -> get % mem
     *      -> get process state
     *      -> is Running in Root? x
     *      -> is known process? o
     *      -> is using webcam? o
     *      -> is using internet x
     *      -> is listening to the keyboard and/or mouse o
     */
    @Override
    public List<Container> scan() {
        List<Container> result = new ArrayList<Container>();

        long[] pids = this.getPids();
        for (long pid : pids) {
            try {
                Container c = new Container("" + pid);
                c.addExtraInfo("Name", this.sigar.getProcState(pid).getName());
                c.addExtraInfo("USER", this.sigar.getProcCredName(pid).getUser());
//                c.addExtraInfo("EXE Name", this.sigar.getProcExe(pid).getName());
//                c.addExtraInfo("EXE CWD", this.sigar.getProcExe(pid).getCwd());
                c.addExtraInfo("Description", ProcUtil.getDescription(this.sigar, pid));

                //process state
                c.addResource(new Resource("Nice", this.sigar.getProcState(pid).getNice()));
                c.addResource(new Resource("Priority", this.sigar.getProcState(pid).getPriority()));
                c.addResource(new Resource("Processor", this.sigar.getProcState(pid).getProcessor()));
                c.addResource(new Resource("Threads", this.sigar.getProcState(pid).getThreads()));
                c.addResource(new Resource("Tty", this.sigar.getProcState(pid).getTty()));
                c.addResource(new Resource("State", this.sigar.getProcState(pid).getState()));

                //CPU
                c.addResource(new Resource("CPU_%", this.sigar.getProcCpu(pid).getPercent()));

                //MEM
                c.addResource(new Resource("MEM_Size", this.sigar.getProcMem(pid).getSize()));
                c.addResource(new Resource("MEM_Resident", this.sigar.getProcMem(pid).getResident()));
                c.addResource(new Resource("MEM_Share", this.sigar.getProcMem(pid).getShare()));

                //booleans
                c.addResource(new Resource("Root?", this.isRunningAsRoot(pid)));
                c.addResource(new Resource("Known?", this.isKnownProcess(pid)));
                c.addResource(new Resource("Webcam?", this.isUsingWebcam(pid)));
                c.addResource(new Resource("Internet?", this.isUsingInternet(pid, this.getProcessesUsingInternet())));
                c.addResource(new Resource("Listening?", this.isListeningToKeyboard(pid)));

                //Add Container to result
                result.add(c);
            } catch (SigarException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return result;
    }
}
