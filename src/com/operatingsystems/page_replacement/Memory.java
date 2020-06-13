package com.operatingsystems.page_replacement;

import java.util.*;

public class Memory {
    private static final Random random = new Random();
    private final ArrayList<Page> pages;
    private final int frameSize;

    public Memory(ArrayList<Page> pages, int frameSize) {
        this.pages = pages;
        this.frameSize = frameSize;
    }

    public Result FIFO() {
        Result result = new Result();

        Queue<Page> remainingPages = new LinkedList<>(pages);
        Queue<Page> frames = new LinkedList<>();

        while(!remainingPages.isEmpty()) {
            Page currentPage = remainingPages.poll();
            if(frames.size() < frameSize) {
                frames.add(currentPage);
                result.miss();
            } else {
                if(frames.contains(currentPage)) {
                    result.hit();
                } else {
                    // we need to empty a slot in our frame
                    frames.poll();
                    frames.add(currentPage);
                    result.miss();
                }
            }
        }

        return result;
    }

    // the optimal solution
    // not suitable for real time
    public Result OPT() {
        Result result = new Result();

        Queue<Page> remainingPages = new LinkedList<>(pages);
        Queue<Page> frames = new LinkedList<>();

        while(!remainingPages.isEmpty()) {
            Page currentPage = remainingPages.poll();
            if(frames.size() < frameSize) {
                frames.add(currentPage);
                result.miss();
            } else {
                if(frames.contains(currentPage)) {
                    result.hit();
                } else {
                    // we need to empty a slot in our frame
                    // replace a frame that's not used in the longest period of time
                    if(!remainingPages.isEmpty()) {
                        frames.remove(findLeastNeededPage(remainingPages));
                        frames.add(currentPage);
                        result.miss();
                    } else {
                        result.hit();
                    }
                }
            }
        }

        return result;
    }

    // least recently used
    public Result LRU() {
        Result result = new Result();

        Queue<Page> remainingPages = new LinkedList<>(pages);
        Queue<Page> frames = new LinkedList<>();
        Queue<Page> usedFrames = new LinkedList<>();

        while(!remainingPages.isEmpty()) {
            Page currentPage = remainingPages.poll();
            usedFrames.add(currentPage);
            if(frames.size() < frameSize) {
                frames.add(currentPage);
                result.miss();
            } else {
                if(frames.contains(currentPage)) {
                    result.hit();
                } else {
                    Page leastRecentlyUsedPage = usedFrames.poll();

                    frames.remove(leastRecentlyUsedPage);
                    frames.add(currentPage);
                    usedFrames.add(currentPage);
                    result.miss();
                }
            }
        }

        return result;
    }

    // second-chance algorithm
    public Result ARLU() {
        Result result = new Result();

        Queue<Page> remainingPages = new LinkedList<>(pages);
        Queue<Page> frames = new LinkedList<>();

        while(!remainingPages.isEmpty()) {
            Page currentPage = remainingPages.poll();
            if(frames.size() < frameSize) {
                frames.add(currentPage);
                result.miss();
            } else {
                if(frames.contains(currentPage)) {
                    result.hit();
                } else {
                    // we need to empty a slot in our frame
                    // find the first slot without second chance
                    // or the last slot with second chance
                    Page victimPage = null;
                    for(Page p: frames) {
                        if(p.getSecondChance()) {
                            p.useSecondChance();
                        } else {
                            victimPage = p;
                        }

                        if(p.getSecondChance() == false) {
                            break;
                        }
                    }

                    frames.remove(victimPage);

                    // newly added page has a second chance
                    currentPage.resetSecondChance();
                    frames.add(currentPage);
                    result.miss();
                }
            }
        }

        return result;
    }

    public Result RAND() {
        Result result = new Result();

        Queue<Page> remainingPages = new LinkedList<>(pages);
        Queue<Page> frames = new LinkedList<>();

        while(!remainingPages.isEmpty()) {
            Page currentPage = remainingPages.poll();
            if(frames.size() < frameSize) {
                frames.add(currentPage);
                result.miss();
            } else {
                if(frames.contains(currentPage)) {
                    result.hit();
                } else {
                    // we need to empty a random slot in our frame
                    Page randomPage = (Page)frames.toArray()[(random.nextInt(frames.size()))];

                    frames.remove(randomPage);
                    frames.add(currentPage);
                    result.miss();
                }
            }
        }

        return result;
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

    public static ArrayList<Page> generateRandomPages(int amount) {
        ArrayList<Page> result = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            int randomAddress = random.nextInt(9) + 1;
            result.add(new Page(randomAddress));
        }

        return result;
    }
}
