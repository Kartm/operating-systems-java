package com.operatingsystems.page_replacement;

import java.util.ArrayList;

public class Result {
    private int hitsCount = 0;
    private int missCount = 0;

    public void hit() {
        hitsCount++;
    }

    public void miss() {
        missCount++;
    }

    public int getHits() {
        return this.hitsCount;
    }

    public int getMisses() {
        return this.missCount;
    }
}
