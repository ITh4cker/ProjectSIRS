package com.sirs.scanner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hyperic.sigar.NetConnection;
import org.hyperic.sigar.NetFlags;
import org.hyperic.sigar.ProcUtil;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.SigarProxyCache;

import com.sirs.scanner.commands.HardcoreScan;

public class SIGARScanner extends SingleScanner {
    private static final int SLEEP_TIME = 1000 * 5;
    private final SigarProxy sigar;
    private final HardcoreScan hardcore = new HardcoreScan();
    private final Set<String> knownProcesses;
    private Map<Long, Double> procCPU = new HashMap<Long, Double>();
    private Set<Long> processesUsingWebcam = new HashSet<Long>();

    public SIGARScanner(String knownProcessesFile) {
        this.sigar = SigarProxyCache.newInstance(new Sigar(), SLEEP_TIME);
        Set<String> procs = new HashSet<String>();
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            URL url = classLoader.getResource(knownProcessesFile);
            BufferedReader file = new BufferedReader(new FileReader(new File(url.getFile())));
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

    private boolean isUsingWebcam(long pid) {
        return this.processesUsingWebcam.contains(pid);
    }

    private void load() {
        String fetch;
        java.util.Scanner in;
        java.util.Scanner line;

        // cpu usage per process
        this.procCPU.clear();
        fetch = this.hardcore.getAllProcessesCPU();
        if (fetch != null) {
            in = new java.util.Scanner(fetch);
            in.nextLine();
            while (in.hasNext()) {
                line = new java.util.Scanner(in.nextLine());
                try {
                    this.procCPU.put(line.nextLong(), line.nextDouble());
                    line.close();
                } catch (Exception e) {
                    line.close();
                    e.printStackTrace();
                    break;
                }
            }
            in.close();
        }

        // processes using webcam
        this.processesUsingWebcam.clear();
        fetch = this.hardcore.getWebcamUsage();
        if (fetch != null) {
            in = new java.util.Scanner(fetch);
            while (in.hasNext()) {
                try {
                    in.next("p");
                    this.processesUsingWebcam.add(in.nextLong());
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
            in.close();
        }
    }

    /**
     * get all processes running<br>
     * for each process do:<br>
     * &nbsp;-> get % cpu<br>
     * &nbsp;-> get % mem<br>
     * &nbsp;-> get process state<br>
     * &nbsp;-> is Running in Root? x<br>
     * &nbsp;-> is known process? o<br>
     * &nbsp;-> is using webcam? o<br>
     * &nbsp;-> is using internet x<br>
     * &nbsp;-> is listening to the keyboard and/or mouse o<br>
     * 
     * @return returns a list of processes (container) each one with a list of resources being used
     */
    @Override
    public List<Container> scan() {
        List<Container> result = new ArrayList<Container>();

        try {
            //load my system scan
            load();

            long[] pids = getPids();
            for (long pid : pids) {
                try {
                    Container c = new Container("" + pid);
                    c.addExtraInfo("Name", this.sigar.getProcState(pid).getName());
                    c.addExtraInfo("Pid", Long.toString(pid));
                    c.addExtraInfo("USER", this.sigar.getProcCredName(pid).getUser());
//                  c.addExtraInfo("EXE Name", this.sigar.getProcExe(pid).getName());
//                  c.addExtraInfo("EXE CWD", this.sigar.getProcExe(pid).getCwd());
                    c.addExtraInfo("Description", ProcUtil.getDescription(this.sigar, pid));

                    //process state
                    c.addResource(new Resource("Nice", this.sigar.getProcState(pid).getNice()));
                    c.addResource(new Resource("Priority", this.sigar.getProcState(pid).getPriority()));
                    c.addResource(new Resource("Processor", this.sigar.getProcState(pid).getProcessor()));
                    c.addResource(new Resource("Threads", this.sigar.getProcState(pid).getThreads()));
                    c.addResource(new Resource("Tty", this.sigar.getProcState(pid).getTty()));
                    c.addResource(new Resource("State", this.sigar.getProcState(pid).getState()));

                    //CPU
                    c.addResource(new Resource("CPU_usage", this.procCPU.get(pid)));
                    c.addResource(new Resource("CPU_idle", this.sigar.getCpuPerc().getIdle() * 100));

                    //TIME
                    c.addResource(new Resource("TIME_total", this.sigar.getProcTime(pid).getTotal()));
                    c.addResource(new Resource("TIME_user", this.sigar.getProcTime(pid).getUser()));
                    c.addResource(new Resource("TIME_sys", this.sigar.getProcTime(pid).getSys()));

                    //MEM
                    c.addResource(new Resource("MEM_Size", this.sigar.getProcMem(pid).getSize()));
                    c.addResource(new Resource("MEM_Resident", this.sigar.getProcMem(pid).getResident()));
                    c.addResource(new Resource("MEM_Share", this.sigar.getProcMem(pid).getShare()));
                    c.addResource(new Resource("MEM_Available", this.sigar.getMem().getActualFree()));
                    c.addResource(new Resource("MEM_Used", this.sigar.getMem().getActualUsed()));

                    //booleans
                    c.addResource(new Resource("Root?", isRunningAsRoot(pid)));
                    c.addResource(new Resource("Known?", isKnownProcess(pid)));
                    c.addResource(new Resource("Webcam?", isUsingWebcam(pid)));
                    c.addResource(new Resource("Internet?", isUsingInternet(pid, getProcessesUsingInternet())));

                    //Add Container to result
                    result.add(c);
                } catch (SigarException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
