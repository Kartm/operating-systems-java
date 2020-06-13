package com.operatingsystems.frame_allocation;

import java.util.*;

public class Memory {
    private static final Random random = new Random();
    private static final int memorySize = 128;

    private static final int minimalProcessSize = 2;
    private static final int maximalProcessSize = memorySize/2;

    private final ArrayList<Process> processes;

    public Memory(ArrayList<Process> processes) {
        this.processes = processes;
    }

    // random frame allocation
    public int random() {
        int[] quotas = getRandomAllocations(processes.size());

        for(int i = 0; i < processes.size(); i++) {
            Process currentProcess = processes.get(i);
            currentProcess.setQuota(quotas[i]);
        }

        return countPageFaults(processes);
    }

    // proportional frame allocation
    public int proportional() {
        int totalProcessesSize = getTotalProcessesSize();
        Queue<Process> remainingProcesses = new LinkedList<>(processes);

        while(!remainingProcesses.isEmpty()) {
            Process currentProcess = remainingProcesses.poll();
            // calculate the quota
            int quota = (int)Math.floor(1.0*currentProcess.getSize()/totalProcessesSize * memorySize);
            currentProcess.setQuota(quota);
        }

        return countPageFaults(processes);
    }

    // firstly, run the proportional allocation
    // then assign the frames proportionally to the page fault count
    public int pageFaultFrequency() {
        this.proportional();

        int totalPageFaults = 0;
        for(Process p: processes) {
            totalPageFaults += p.countPageFaults();
        }

        Queue<Process> remainingProcesses = new LinkedList<>(processes);

        while(!remainingProcesses.isEmpty()) {
            Process currentProcess = remainingProcesses.poll();
            // calculate the quota
            // the more page faults, the smaller frame size we should assign
            double pageFaultRate = 1.0 * currentProcess.countPageFaults()/totalPageFaults;
            int quota = (int)Math.floor((1.0/pageFaultRate) * memorySize);
            currentProcess.setQuota(1/quota);
        }

        return countPageFaults(processes);
    }

    @SuppressWarnings("unchecked")
    public int workingSet() {
        int smallestPageFaultCount = Integer.MAX_VALUE;
        int k = -1;
        // we don't know "k", so we try to find the most
        // suitable value
        for(double multiplier = 0.01; multiplier < 1.0; multiplier += 0.01) {
            Queue<Process> remainingProcesses = new LinkedList<>(processes);
            int totalDemand = 0;
            Map<Process, Integer> processDemands = new HashMap<Process, Integer>();

            while(!remainingProcesses.isEmpty()) {
                Process currentProcess = remainingProcesses.poll();

                // the working set has to contain
                // all the actively used pages
                // therefore select "k" most frequently used pages
                // their total amount is the demand of the process

                Set<Page> pages = new TreeSet<>(currentProcess.getPages());
                k = (int)Math.round(pages.size() * multiplier);
                if(k == 0) {
                    k = 1;
                }

                // sort pages by occurrences
                ArrayList<Page> sortedPages = new ArrayList<>(pages);
                sortedPages.sort(Comparator.comparingInt((page -> Collections.frequency(currentProcess.getPages(), page))));

                int demand = 0;
                // select k most frequently used pages
                for(int i = 0; i < k && i < sortedPages.size(); i++) {
                    demand += Collections.frequency(currentProcess.getPages(), sortedPages.get(i));
                }

                processDemands.put(currentProcess, demand);
            }

            processDemands.forEach((process, demand) -> {
                int quota = (int)Math.floor((1.0 * demand / totalDemand) * memorySize);
                process.setQuota(quota);
            });

            int pageFaults =  countPageFaults(processes);
            if(pageFaults < smallestPageFaultCount) {
                smallestPageFaultCount = pageFaults;
            }
        }

        // System.out.println(k);
        return smallestPageFaultCount;
    }

    private int[] getRandomAllocations(int n) {
        int[] quotas = new int[n];
        // begin with allocating the minimum size
        Arrays.fill(quotas, minimalProcessSize);

        int remainingSize = memorySize - (minimalProcessSize * n);

        // generate random values in cycles until we run out of free memory
        for(int i = 0; remainingSize > 0; i++) {
            int index = i % n;
            // random int from 0 to remainingSize inclusive
            int randomSize = random.nextInt((remainingSize) + 1);
            quotas[index] += randomSize;
            remainingSize -= randomSize;
        }

        return quotas;
    }

    private int getTotalProcessesSize() {
        int sum = 0;
        for(Process p: processes) {
            sum += p.getSize();
        }
        return sum;
    }

    private int countPageFaults(ArrayList<Process> processes) {
        int sum = 0;
        for(Process p: processes) {
            sum += p.countPageFaults();
        }
        return sum;
    }

    public static ArrayList<Process> generateRandomProcesses(int amount) {
        ArrayList<Process> result = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            int size = random.nextInt(maximalProcessSize) + minimalProcessSize;
            Process process = new Process(size);
            ArrayList<Page> pages = generateRandomPages(size, size);
            process.setPages(pages);

            result.add(process);
        }

        return result;
    }

    public static ArrayList<Page> generateRandomPages(int amount, int maxAddress) {
        ArrayList<Page> result = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            // todo assign addresses within the quota!
            int randomAddress = random.nextInt(maxAddress) + 1;
            result.add(new Page(randomAddress));
        }

        return result;
    }
}
