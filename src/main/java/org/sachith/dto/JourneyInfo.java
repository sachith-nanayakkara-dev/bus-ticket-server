package org.sachith.dto;

public class JourneyInfo {

    private String origin;

    private String destination;

    private String estimatedDepartureTime;

    private String estimatedArrivalTime;

    public JourneyInfo(
            String origin,
            String destination,
            String estimatedDepartureTime,
            String estimatedArrivalTime) {

        this.origin = origin;
        this.destination = destination;
        this.estimatedDepartureTime = estimatedDepartureTime;
        this.estimatedArrivalTime = estimatedArrivalTime;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getEstimatedDepartureTime() {
        return estimatedDepartureTime;
    }

    public String getEstimatedArrivalTime() {
        return estimatedArrivalTime;
    }
}
