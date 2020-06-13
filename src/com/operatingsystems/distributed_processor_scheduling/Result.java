package com.operatingsystems.distributed_processor_scheduling;

import java.util.Arrays;

public class Result {
    private int time;
    private int requestCount = 0;
    private int migrationCount = 0;
    private double[] loadFactors;

    public Result() {

    }

    public void incrementRequests() {
        requestCount++;
    }

    public void incrementMigrations() {
        migrationCount++;
    }

    public void setLoadFactors(double[] loadFactors) {
        this.loadFactors = loadFactors;
    }

    public double[] getAverageLoadFactors() {
        double[] averageLoadFactors = new double[loadFactors.length];
        for(int i = 0; i < loadFactors.length; i++) {
            averageLoadFactors[i] = 1.0 * loadFactors[i] / time;
        }
        return averageLoadFactors;
    }

    public void setTotalTime(int time) {
        this.time = time;
    }

    public String toString() {
        return Arrays.toString(this.getAverageLoadFactors()) + " " + this.requestCount + " " + this.migrationCount;
    }
}
