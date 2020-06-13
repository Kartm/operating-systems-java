package com.operatingsystems.distributed_processor_scheduling;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        ArrayList<Processor> processors = new ArrayList<>(Arrays.asList(new Processor(1),
                new Processor(2),
                new Processor(3),
                new Processor(4)));

        processors.get(0).addProcess(new Process(0, 120));
        processors.get(0).addProcess(new Process(1, 30));
        processors.get(1).addProcess(new Process(2, 10));
        processors.get(1).addProcess(new Process(3, 40));
        processors.get(1).addProcess(new Process(4, 25));
        processors.get(1).addProcess(new Process(5, 60));
        processors.get(1).addProcess(new Process(6, 90));
        processors.get(1).addProcess(new Process(7, 25));
        processors.get(1).addProcess(new Process(8, 5));
        processors.get(1).addProcess(new Process(9, 20));
        processors.get(1).addProcess(new Process(10, 500));
        processors.get(2).addProcess(new Process(11, 120));
        processors.get(2).addProcess(new Process(12, 30));
        processors.get(2).addProcess(new Process(13, 20));
        processors.get(2).addProcess(new Process(14, 90));
        processors.get(2).addProcess(new Process(15, 100));
        processors.get(2).addProcess(new Process(16, 200));
        processors.get(2).addProcess(new Process(17, 400));
        processors.get(2).addProcess(new Process(18, 10));
        processors.get(2).addProcess(new Process(19, 5));
        processors.get(2).addProcess(new Process(20, 5));
        processors.get(2).addProcess(new Process(21, 5));
        processors.get(3).addProcess(new Process(22, 100));
        processors.get(3).addProcess(new Process(23, 100));
        processors.get(3).addProcess(new Process(24, 5));

        DistributedSystem distributedSystem = new DistributedSystem(processors);
        // connect every processor to each other
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                // skip connecting processor to itself
                if (i != j) {
                    distributedSystem.connectProcessors(processors.get(i), processors.get(j));
                }
            }
        }

        System.out.println(distributedSystem.noOptimization());
        System.out.println(distributedSystem.random());
        System.out.println(distributedSystem.maximumThreshold());
        System.out.println(distributedSystem.minimumThreshold());

    }
}
