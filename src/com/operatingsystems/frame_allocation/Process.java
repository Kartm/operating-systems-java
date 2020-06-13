package com.operatingsystems.frame_allocation;

import java.util.*;

public class Process implements Comparable<Process> {
    private final int size;
    private int quota = -1; // -1 means unset
    private ArrayList<Page> pages;

    public Process(int size) {
        this.size = size;
    }

    public int getSize() {
        return this.size;
    }

    public void setPages(ArrayList<Page> pages) {
        this.pages = pages;
    }

    public ArrayList<Page> getPages() {
        return this.pages;
    }

    // using OPT (optimal algorithm) from previous assignment
    public int countPageFaults() {
        int pageFaultCounter = 0;

        Queue<Page> remainingPages = new LinkedList<>(pages);
        Queue<Page> frames = new LinkedList<>();

        while(!remainingPages.isEmpty()) {
            Page currentPage = remainingPages.poll();
            if(frames.size() < quota) {
                frames.add(currentPage);
                pageFaultCounter++;
            } else {
                if(!frames.contains(currentPage)) {
                    // we need to empty a slot in our frame
                    // replace a frame that's not used in the longest period of time
                    if(!remainingPages.isEmpty()) {
                        frames.remove(findLeastNeededPage(remainingPages));
                        frames.add(currentPage);
                        pageFaultCounter++;
                    }
                }
            }
        }

        return pageFaultCounter;
    }

    private Page findLeastNeededPage(Queue<Page> page) {
        // remove duplicates
        // use LinkedHashSet to preserve the order
        Set<Page> set = new LinkedHashSet<>(page);

        // return last element of set
        Page lastPage = null;

        for (Page value : set) {
            lastPage = value;
        }

        return lastPage;
    }

    public void setQuota(int size) {
        this.quota = size;
    }

    public int getQuota() {
        return this.quota;
    }

    @Override
    public String toString() {
        return String.valueOf(this.getSize());
    }

    @Override
    public int compareTo(Process page) {
        return Integer.compare(this.getSize(), page.getSize());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() == Process.class && obj != null) {
            Process other = (Process)obj;
            return other.getSize() == this.getSize();
        }
        return false;
    }
}
