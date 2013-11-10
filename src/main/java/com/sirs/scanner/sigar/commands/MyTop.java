package com.sirs.scanner.sigar.commands;

import java.util.ArrayList;
import java.util.List;

import org.hyperic.sigar.ProcUtil;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;

import com.sirs.scanner.Container;
import com.sirs.scanner.Resource;

public class MyTop {

    public static List<Container> execute(final SigarProxy sigar) {
        List<Container> result = new ArrayList<Container>();
        for (long pid : MyTop.getPids(sigar)) {
            Container c = new Container("pid: " + pid);
            try {
                //process state
                c.addResource(new Resource("Name", sigar.getProcState(pid).getName()));
                c.addResource(new Resource("Nice", sigar.getProcState(pid).getNice()));
                c.addResource(new Resource("Priority", sigar.getProcState(pid).getPriority()));
                c.addResource(new Resource("Processor", sigar.getProcState(pid).getProcessor()));
                c.addResource(new Resource("Threads", sigar.getProcState(pid).getThreads()));
                c.addResource(new Resource("Tty", sigar.getProcState(pid).getTty()));
                c.addResource(new Resource("State", sigar.getProcState(pid).getState()));

                //CPU
//                c.addResource(new Resource("CPU %", sigar.getProcCpu(pid).getPercent()));
//                c.addResource(new Resource("CPU Last Time", sigar.getProcCpu(pid).getLastTime()));
//                c.addResource(new Resource("CPU Start Time", sigar.getProcCpu(pid).getStartTime()));
//                c.addResource(new Resource("CPU Sys", sigar.getProcCpu(pid).getSys()));
                c.addResource(new Resource("CPU Total", sigar.getProcCpu(pid).getTotal()));
//                c.addResource(new Resource("CPU User", sigar.getProcCpu(pid).getUser()));

                //MEM
                c.addResource(new Resource("MEM Size", Sigar.formatSize(sigar.getProcMem(pid).getSize())));
                c.addResource(new Resource("MEM Resident", Sigar.formatSize(sigar.getProcMem(pid).getResident())));
                c.addResource(new Resource("MEM Share", Sigar.formatSize(sigar.getProcMem(pid).getShare())));

                //Cred
                c.addResource(new Resource("USER", sigar.getProcCredName(pid).getUser()));

//                //Exe ???
//                c.addResource(new Resource("EXE Name", sigar.getProcExe(pid).getName()));
//                c.addResource(new Resource("EXE CWD", sigar.getProcExe(pid).getCwd()));

//                c.addResource(new Resource("FD", sigar.getProcFd(pid).toString()));

                c.addResource(new Resource("Description", ProcUtil.getDescription(sigar, pid)));

            } catch (SigarException e) {
                e.printStackTrace();
                continue;
            }
            result.add(c);
        }

        return result;
    }

    private static long[] getPids(final SigarProxy sigar) {
        try {
            return sigar.getProcList();
        } catch (SigarException e) {
            e.printStackTrace();
            return new long[0];
        }
    }
}
