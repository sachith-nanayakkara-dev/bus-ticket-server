package org.sachith.dto;

import java.util.List;

public class GridSeatMapResponse {

    private String origin;
    private String destination;
    private String travelDate;
    private List<List<GridSeat>> rows;

    public GridSeatMapResponse(
            String origin,
            String destination,
            String travelDate,
            List<List<GridSeat>> rows) {

        this.origin = origin;
        this.destination = destination;
        this.travelDate = travelDate;
        this.rows = rows;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public List<List<GridSeat>> getRows() {
        return rows;
    }

    public String getTravelDate() {
        return travelDate;
    }
}
