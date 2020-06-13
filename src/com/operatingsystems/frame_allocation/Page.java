package com.operatingsystems.frame_allocation;

public class Page implements Comparable<Page> {
    private final int address;
    private int timer;

    public Page(int address) {
        this.address = address;
    }

    public int getAddress() {
        return this.address;
    }

    public void setTimer(int time) {
        this.timer = time;
    }

    public int getTimer() {
        return this.timer;
    }

    public void decrementTimer() {
        this.timer--;
    }

    @Override
    public String toString() {
        return String.valueOf(this.getAddress());
    }

    @Override
    public int compareTo(Page page) {
        return Integer.compare(this.getAddress(), page.getAddress());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() == Page.class && obj != null) {
            Page other = (Page)obj;
            return other.getAddress() == this.getAddress();
        }
        return false;
    }
}
