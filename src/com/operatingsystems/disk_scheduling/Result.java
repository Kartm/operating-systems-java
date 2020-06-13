package com.operatingsystems.disk_scheduling;

import java.util.ArrayList;

public class Result {
    private int totalMovementTime = 0;
    private int requestsAmount;
    private ArrayList<Request> order = new ArrayList<>();

    public Result(int requestsAmount) {
        this.requestsAmount = requestsAmount;
    }

    public void resolveRequest(Request request, int movementNeeded) {
        this.order.add(request);
        this.totalMovementTime += movementNeeded;
    }

    public ArrayList<Request> getOrder() {
        return this.order;
    }

    public void increaseTotalMovement(int amount) {
        this.totalMovementTime += amount;
    }

    public double getTotalMovementTime() {
        return this.totalMovementTime;
    }

    public double getAverageSeekTime() {
        return 1.0 * this.totalMovementTime / requestsAmount;
    }
}
