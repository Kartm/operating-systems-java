package com.operatingsystems.distributed_processor_scheduling;

import java.util.*;
import java.util.stream.Collectors;

public class DistributedSystem {
    private static final Random random = new Random();

    private static ArrayList<Processor> processors = new ArrayList<>();
    private final Map<Processor, Integer> processorIndexes = new HashMap<>();
    private final ArrayList<ArrayList<Integer>> adjacencyList = new ArrayList<>();

    public DistributedSystem(ArrayList<Processor> newProcessors) {
        processors = newProcessors;
        for(int i = 0; i < processors.size(); i++) {
            processorIndexes.put(processors.get(i), i);
            adjacencyList.add(new ArrayList<Integer>());
        }
    }

    public void connectProcessors(Processor a, Processor b) {
        int indexA = processorIndexes.get(a);
        int indexB = processorIndexes.get(b);

        adjacencyList.get(indexA).add(indexB);
        adjacencyList.get(indexB).add(indexA);
    }

    public Result noOptimization() {
        Result result = new Result();

        int time = 0;
        double[] loadFactors = new double[4];
        ArrayList<Processor> processors = cloneProcessors(this.processors);

        while(!isEveryProcessorFinished(processors)) {
            for(int i = 0; i < processors.size(); i++) {
                Processor currentProcessor = processors.get(i);
                loadFactors[i] += currentProcessor.getLoadFactor();
                currentProcessor.execute();
            }
            time++;
        }

        result.setTotalTime(time);
        result.setLoadFactors(loadFactors);
        return result;
    }

    public Result random() {
        Result result = new Result();

        int time = 0;
        double[] loadFactors = new double[4];
        ArrayList<Processor> processors = cloneProcessors(this.processors);

        while(!isEveryProcessorFinished(processors)) {
            for(int i = 0; i < processors.size(); i++) {
                Processor currentProcessor = processors.get(i);
                loadFactors[i] += currentProcessor.getLoadFactor();
                currentProcessor.execute();

                // if overloaded, move one of the processes
                // to random neighbor
                if(currentProcessor.getLoadFactor() > 1.0) {
                    Processor randomNeighbor = randomNeighbor(processors, currentProcessor.getId());
                    //System.out.println(currentProcessor.getId() + " overload, moving to " + randomNeighbor.getId());
                    Process processToMove = currentProcessor.getCurrentProcess();
                    if(processToMove != null) {
                        currentProcessor.removeProcess(processToMove);
                        randomNeighbor.addProcess(processToMove);

                        result.incrementRequests();
                        result.incrementMigrations();
                    }

                }
            }
            time++;
        }

        result.setTotalTime(time);
        result.setLoadFactors(loadFactors);
        return result;
    }

    // receiver initiated
    // find overloaded processor and steal its process
    // but only if current processor is not overloaded
    private static final double MINIMAL_THRESHOLD = 0.2;
    private static final double MAXIMUM_THRESHOLD = 0.8;
    private static final int PROBING_LIMIT = 4;

    public Result maximumThreshold() {
        Result result = new Result();

        int time = 0;
        double[] loadFactors = new double[4];
        ArrayList<Processor> processors = cloneProcessors(this.processors);

        while(!isEveryProcessorFinished(processors)) {
            for(int i = 0; i < processors.size(); i++) {
                Processor currentProcessor = processors.get(i);
                loadFactors[i] += currentProcessor.getLoadFactor();
                currentProcessor.execute();

                // if not overloaded
                if(currentProcessor.getLoadFactor() < MAXIMUM_THRESHOLD) {
                    Processor randomNeighbor = null;
                    result.incrementRequests();
                    // find overloaded neighbor
                    // check if stealing its process helps it fall under the threshold
                    for(int probingIndex = 0; probingIndex < PROBING_LIMIT; probingIndex++) {
                        result.incrementRequests();
                        randomNeighbor = randomNeighbor(processors, currentProcessor.getId());
                        if(randomNeighbor.getLoadFactor() > MAXIMUM_THRESHOLD
                                && randomNeighbor.checkPotentialLoadFactor() <= MAXIMUM_THRESHOLD) {
                            break;
                        }
                    }

                    if(randomNeighbor != null) {
                        Process processToMove = randomNeighbor.getCurrentProcess();
                        if(processToMove != null) {
                            randomNeighbor.removeProcess(processToMove);
                            currentProcessor.addProcess(processToMove);

                            result.incrementMigrations();
                        }

                    }


                }
            }
            time++;
        }

        result.setTotalTime(time);
        result.setLoadFactors(loadFactors);
        return result;
    }

    public Result minimumThreshold() {
        Result result = new Result();

        int time = 0;
        double[] loadFactors = new double[4];
        ArrayList<Processor> processors = cloneProcessors(this.processors);

        while(!isEveryProcessorFinished(processors)) {
            for(int i = 0; i < processors.size(); i++) {
                Processor currentProcessor = processors.get(i);
                loadFactors[i] += currentProcessor.getLoadFactor();
                currentProcessor.execute();

                // if not overloaded
                if(currentProcessor.getLoadFactor() < MAXIMUM_THRESHOLD) {
                    Processor randomNeighbor = null;
                    result.incrementRequests();
                    // find overloaded neighbor
                    // check if stealing its process helps it fall under the threshold
                    for(int probingIndex = 0; probingIndex < PROBING_LIMIT; probingIndex++) {
                        result.incrementRequests();
                        randomNeighbor = randomNeighbor(processors, currentProcessor.getId());
                        if(randomNeighbor.getLoadFactor() > MAXIMUM_THRESHOLD
                                && randomNeighbor.checkPotentialLoadFactor() > MINIMAL_THRESHOLD) {
                            break;
                        }
                    }

                    if(randomNeighbor != null) {
                        Process processToMove = randomNeighbor.getCurrentProcess();
                        if(processToMove != null) {
                            randomNeighbor.removeProcess(processToMove);
                            currentProcessor.addProcess(processToMove);

                            result.incrementMigrations();
                        }

                    }


                }
            }
            time++;
        }

        result.setTotalTime(time);
        result.setLoadFactors(loadFactors);
        return result;
    }

    private Processor randomNeighbor(ArrayList<Processor> processors, int exceptId) {
        Processor randomProcessor = null;

        while(randomProcessor == null || randomProcessor.getId() == exceptId) {
            randomProcessor = processors.get(random.nextInt(processors.size()));
        }

        return randomProcessor;
    }

    private ArrayList<Processor> cloneProcessors(ArrayList<Processor> source) {
        ArrayList<Processor> result = new ArrayList<>();
        for(Processor processor: source) {
            Processor newProcessor = new Processor(processor.getId());
            for(Process process: processor.getProcesses()) {
                newProcessor.addProcess(new Process(process.getId(), process.getTimeLeft()));
            }
            result.add(newProcessor);
        }
        return result;
    }

    private boolean isEveryProcessorFinished(ArrayList<Processor> processors) {
        for(Processor p: processors) {
            if(!p.isFinished()) {
                return false;
            }
        }
        return true;
    }
}
