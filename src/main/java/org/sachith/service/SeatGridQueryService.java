package org.sachith.service;

import org.sachith.dto.GridSeatMapResponse;

public interface SeatGridQueryService {

    GridSeatMapResponse getGridSeatMap(
            String origin,
            String destination,
            String travelDate);
}
