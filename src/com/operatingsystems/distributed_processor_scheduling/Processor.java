package com.operatingsystems.distributed_processor_scheduling;

import java.util.ArrayList;

public class Processor {
    // for example 5 processes will mean we have 83% load
    private static final int processCapacity = 6;

    private final int id;
    private ArrayList<Process> processes = new ArrayList<>();

    public Processor(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public ArrayList<Process> getProcesses() {
        return this.processes;
    }

    public double getLoadFactor() {
        return 1.0 * processes.size() / processCapacity;
    }

    public void addProcess(Process process) {
        this.processes.add(process);
    }

    public void removeProcess(Process process) {
        this.processes.remove(process);
    }

    // returns the load factor
    // if we were to remove a process
    public double checkPotentialLoadFactor() {
        if(processes.size() <= 1) {
            return 0;
        } else {
            return 1.0 * (processes.size()) / processCapacity;
        }
    }

    // executes one pass of ROT algorithm
    public void execute() {
        for(Process p: processes) {
            if(!p.isFinished()) {
                p.doWork();
            }
        }
    }

    // returns first unfinished process
    public Process getCurrentProcess() {
        for(Process p: processes) {
            if(!p.isFinished()) {
                return p;
            }
        }
        return null;
    }

    public boolean isFinished() {
        for(Process p: processes) {
            if(!p.isFinished()) {
                return false;
            }
        }
        return true;
    }
}
