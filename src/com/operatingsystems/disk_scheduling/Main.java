package com.operatingsystems.disk_scheduling;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        System.out.println("Randomly generated requests [id, (position)]: ");
        //ArrayList<Request> requests = Disk.generateRequests(10);
        //System.out.println(requests);

        //Disk disk = new Disk(requests);
        System.out.println("Total movement time | Average movement time | Order");
        /*
        Result fcfs = disk.fcfs();
        System.out.println("FCFS: " + fcfs.getTotalMovementTime() + " " + fcfs.getAverageSeekTime() + " " + fcfs.getOrder());

        Result sstf = disk.sstf();
        System.out.println("SSTF: " + sstf.getTotalMovementTime() + " " + sstf.getAverageSeekTime() + " " + sstf.getOrder());

        Result scan = disk.scan();
        System.out.println("SCAN: " + scan.getTotalMovementTime() + " " + scan.getAverageSeekTime() + " " + scan.getOrder());

        Result cscan = disk.cscan();
        System.out.println("CSCAN: " + cscan.getTotalMovementTime() + " " + cscan.getAverageSeekTime() + " " + cscan.getOrder());

        Result edf = disk.edf();
        System.out.println("EDF: " + edf.getTotalMovementTime() + " " + edf.getAverageSeekTime() + " " + edf.getOrder());*/

        for(int i = 0; i < 5; i++) {
            ArrayList<Request> requests = Disk.generateRequests(5000);
            Disk disk = new Disk(requests);
            Result fcfs = disk.fcfs();
            Result sstf = disk.sstf();
            Result scan = disk.scan();
            Result cscan = disk.cscan();
            Result edf = disk.edf();
            System.out.println(fcfs.getTotalMovementTime() + ";" +
                    sstf.getTotalMovementTime() + ";" +
                    scan.getTotalMovementTime() + ";" +
                    cscan.getTotalMovementTime() + ";" +
                    edf.getTotalMovementTime()
            );
        }
    }
}
