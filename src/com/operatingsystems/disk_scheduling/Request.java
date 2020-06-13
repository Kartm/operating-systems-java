package com.operatingsystems.disk_scheduling;

public class Request implements Comparable<Request> {
    private final int id;
    private final int position;
    private final int deadline;

    public Request(int id, int position, int deadline) {
        this.id = id;
        this.position = position;
        this.deadline = deadline;
    }

    public int getId() {
        return this.id;
    }

    public int getDeadline() {
        return this.deadline;
    }

    public int getPosition() {
        return this.position;
    }

    @Override
    public String toString() {
        return this.getId() + ";" + this.getPosition() + ";" + this.getDeadline();
    }

    @Override
    public int compareTo(Request request) {
        return Integer.compare(this.getPosition(), request.getPosition());
    }
}
