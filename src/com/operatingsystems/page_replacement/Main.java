package com.operatingsystems.page_replacement;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        final int frameSize = 3;
        //ArrayList<Page> pages = Memory.generateRandomPages(5);

        // reference string from the book added four times
        // and then added in reverse
        // 4 7 0 7 1 0 1 2 1 2 2
        int[] references = new int[]{4, 7, 0, 7, 1, 0, 1, 2, 1, 2, 2};

        ArrayList<Page> pages = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < references.length; j++) {
                pages.add(new Page(references[j]));
            }
        }
        // add in reversed order
        for(int j = references.length - 1; j >= 0; j--) {
            pages.add(new Page(references[j]));
        }

        Memory memory = new Memory(pages, frameSize);

        Result fifo = memory.FIFO();
        System.out.println("FIFO: " + fifo.getHits() + " hits, " + fifo.getMisses() + " misses");

        Result opt = memory.OPT();
        System.out.println("OPT: " + opt.getHits() + " hits, " + opt.getMisses() + " misses");

        Result lru = memory.LRU();
        System.out.println("LRU: " + lru.getHits() + " hits, " + lru.getMisses() + " misses");

        Result arlu = memory.ARLU();
        System.out.println("ARLU: " + arlu.getHits() + " hits, " + arlu.getMisses() + " misses");

        Result rand = memory.RAND();
        System.out.println("RAND: " + rand.getHits() + " hits, " + rand.getMisses() + " misses");
    }
}
