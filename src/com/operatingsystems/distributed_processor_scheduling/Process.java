package com.operatingsystems.distributed_processor_scheduling;

import java.util.*;

public class Process implements Comparable<Process> {
    private final int id;
    private int timeLeft;

    public Process(int id, int neededTime) {
        this.id = id;
        this.timeLeft = neededTime;
    }

    public int getId() {
        return this.id;
    }

    public int getTimeLeft() {
        return this.timeLeft;
    }

    public void doWork() {
        this.timeLeft--;
    }

    public boolean isFinished() {
        return this.timeLeft == 0;
    }

    @Override
    public String toString() {
        return String.valueOf(this.getId()) + "(" + this.getTimeLeft() + ")";
    }

    @Override
    public int compareTo(Process page) {
        return Integer.compare(this.getId(), page.getId());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() == Process.class && obj != null) {
            Process other = (Process)obj;
            return other.getId() == this.getId();
        }
        return false;
    }
}
