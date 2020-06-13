package com.operatingsystems.page_replacement;

public class Page implements Comparable<Page> {
    private final int address;
    private boolean secondChance = true;

    public Page(int address) {
        this.address = address;
    }

    public int getAddress() {
        return this.address;
    }

    public boolean getSecondChance() {
        return this.secondChance;
    }

    public void resetSecondChance() {
        this.secondChance = true;
    }

    public void useSecondChance() {
        this.secondChance = false;
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
