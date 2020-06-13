package com.operatingsystems.disk_scheduling;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class Disk {
    private static final Random random = new Random();
    private final ArrayList<Request> requests;

    private static final int MAX_POSITION = 20000;
    private static final int startPosition = MAX_POSITION / 2;

    public enum Direction {
        LEFT,
        RIGHT
    }

    public Disk(ArrayList<Request> requests) {
        this.requests = requests;
    }

    // first come, first serve
    public Result fcfs() {
        int position = startPosition;
        Result result = new Result(requests.size());
        for (Request r : requests) {
            // get cost of going to the request
            int cost = Math.abs(position - r.getPosition());

            result.resolveRequest(r, cost);

            // move on
            position = r.getPosition();
        }
        return result;
    }

    // goes in the order of smallest deadlines
    @SuppressWarnings("unchecked")
    public Result edf() {
        int position = startPosition;

        Result result = new Result(requests.size());
        ArrayList<Request> sortedRequests = (ArrayList<Request>) requests.clone();
        sortedRequests.sort(Comparator.comparingInt(Request::getDeadline));
        for(Request r: sortedRequests) {
            int cost = Math.abs(position - r.getPosition());
            result.resolveRequest(r, cost);
        }
        return result;
    }

    // shortest seek time first
    @SuppressWarnings("unchecked")
    public Result sstf() {
        int position = startPosition;
        Result result = new Result(requests.size());
        ArrayList<Request> requestsLeft = (ArrayList<Request>) requests.clone();

        // until we resolve every request
        while(!requestsLeft.isEmpty()) {
            Request nearestRequest = getNearestRequest(position, requestsLeft);

            requestsLeft.remove(nearestRequest);
            result.resolveRequest(nearestRequest, getDistance(position, nearestRequest));
            position = nearestRequest.getPosition();
        }

        return result;
    }

    // goes to the nearest, then scans to left, to right and so on
    @SuppressWarnings("unchecked")
    public Result scan() {
        Result result = new Result(requests.size());

        ArrayList<Request> toVisitLeft = new ArrayList<>();
        ArrayList<Request> toVisitRight = new ArrayList<>();

        for(Request r: requests) {
            if(r.getPosition() < startPosition) {
                toVisitLeft.add(r);
            } else {
                toVisitRight.add(r);
            }
        }

        toVisitLeft.sort(Comparator.comparingInt(Request::getPosition));
        toVisitRight.sort(Comparator.comparingInt(Request::getPosition).reversed());


        // start with the nearest request
        int position = startPosition;
        Direction direction = position >= getNearestRequest(position, requests).getPosition()
                ? Direction.LEFT: Direction.RIGHT;
        // then continue moving in its direction

        // two runs (both directions)
        for(int i = 0; i < 2; i++) {
            if(direction == Direction.LEFT) {
                while(!toVisitLeft.isEmpty()) {
                    Request r = toVisitLeft.get(0);
                    toVisitLeft.remove(0);

                    result.resolveRequest(r, Math.abs(position - r.getPosition()));
                    position = r.getPosition();
                }
                direction = Direction.RIGHT;
            } else {
                while(!toVisitRight.isEmpty()) {
                    Request r = toVisitRight.get(0);
                    toVisitRight.remove(0);

                    result.resolveRequest(r, Math.abs(position - r.getPosition()));
                    position = r.getPosition();
                }
                direction = Direction.LEFT;
            }
        }

        return result;
    }

    // similarly to the above, but once reaching the left/right jumps to the other side
    // the jump doesnt count as head movement
    @SuppressWarnings("unchecked")
    public Result cscan() {
        Result result = new Result(requests.size());

        ArrayList<Request> toVisitLeft = new ArrayList<>();
        ArrayList<Request> toVisitRight = new ArrayList<>();

        for(Request r: requests) {
            if(r.getPosition() < startPosition) {
                toVisitLeft.add(r);
            } else {
                toVisitRight.add(r);
            }
        }

        toVisitLeft.sort(Comparator.comparingInt(Request::getPosition));
        toVisitRight.sort(Comparator.comparingInt(Request::getPosition));


        // start with the nearest request
        int position = startPosition;
        Direction direction = position >= getNearestRequest(position, requests).getPosition()
                ? Direction.LEFT: Direction.RIGHT;
        // then continue moving in its direction

        // only in one direction
        for(int i = 0; i < 2; i++) {
            if(direction == Direction.LEFT) {
                while(!toVisitLeft.isEmpty()) {
                    Request r = toVisitLeft.get(0);
                    toVisitLeft.remove(0);

                    result.resolveRequest(r, Math.abs(position - r.getPosition()));
                    position = r.getPosition();
                }
                while(!toVisitRight.isEmpty()) {
                    Request r = toVisitRight.get(0);
                    toVisitRight.remove(0);

                    result.resolveRequest(r, Math.abs(position - r.getPosition()));
                    position = r.getPosition();
                }
            } else {
                while(!toVisitRight.isEmpty()) {
                    Request r = toVisitRight.get(0);
                    toVisitRight.remove(0);

                    result.resolveRequest(r, Math.abs(position - r.getPosition()));
                    position = r.getPosition();
                }
                while(!toVisitLeft.isEmpty()) {
                    Request r = toVisitLeft.get(0);
                    toVisitLeft.remove(0);

                    result.resolveRequest(r, Math.abs(position - r.getPosition()));
                    position = r.getPosition();
                }
            }
        }

        return result;
    }

    private Request getNearestRequest(int position, ArrayList<Request> requests) {
        requests.sort(Comparator.comparingInt((Request a) -> getDistance(position, a)));
        return requests.get(0);
    }

    private int getDistance(int position, Request request) {
        return Math.abs(position - request.getPosition());
    }

    public static ArrayList<Request> generateRequests(int amount) {
        ArrayList<Request> result = new ArrayList<>();
        int majorityOfRequests = (int)Math.floor(amount * 0.8);
        for (int i = 0; i < majorityOfRequests; i++) {
            while(true) {
                int randomPosition = random.nextInt(MAX_POSITION) + 1;
                int randomDeadline = random.nextInt(100) + 1;
                if(!isPositionTaken(randomPosition, result)) {
                    result.add(new Request(i, randomPosition, randomDeadline));
                    break;
                }
            }
        }
        // add a bunch of more "packed" requests
        for (int i = 0; i < amount - majorityOfRequests; i++) {
            while(true) {
                int randomPosition = random.nextInt((int)Math.floor(MAX_POSITION * 0.2)) + 1;
                int randomDeadline = random.nextInt(100) + 1;
                if(!isPositionTaken(randomPosition, result)) {
                    result.add(new Request(i, randomPosition, randomDeadline));
                    break;
                }
            }
        }
        return result;
    }

    private static boolean isPositionTaken(int position, ArrayList<Request> requests) {
        for(Request element: requests) {
            if(element.getPosition() == position) {
                return true;
            }
        }
        return false;
    }
}
