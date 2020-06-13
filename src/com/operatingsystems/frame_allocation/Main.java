package com.operatingsystems.frame_allocation;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<Process> processes = Memory.generateRandomProcesses(9);

        System.out.println("Randomly generated processes: ");
        System.out.println(processes);

        Memory memory = new Memory(processes);

        System.out.println("Proportional: " + memory.proportional());
        System.out.println("Random: " + memory.random());
        System.out.println("Page fault count: " + memory.pageFaultFrequency());
        System.out.println("Working set: " + memory.workingSet());

        for(int i = 0; i < 5; i++) {
            System.out.println(memory.proportional() + " " + memory.random() + " " + memory.pageFaultFrequency() + " " + memory.workingSet());
        }

    }
}
