package org.sachith.service;

import org.sachith.dto.GridSeat;
import org.sachith.dto.GridSeatMapResponse;
import org.sachith.model.Seat;
import org.sachith.model.Trip;
import org.sachith.repository.TripRepository;
import org.sachith.repository.TripRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

public class SeatGridQueryServiceImpl implements SeatGridQueryService {

    private final TripRepository tripRepository =
            new TripRepositoryImpl();

    @Override
    public GridSeatMapResponse getGridSeatMap(
            String origin,
            String destination,
            String travelDate
    ) {

        int originIndex = index(origin);
        int destinationIndex = index(destination);

        boolean isForward = originIndex < destinationIndex;

        int start = Math.min(index(origin), index(destination));
        int end = Math.max(index(origin), index(destination));

        Trip trip =
                tripRepository.findOrCreate(travelDate, isForward);

        List<List<GridSeat>> grid = new ArrayList<>();

        List<Seat> seats = trip.getSeats();

        for (int row = 1; row <= 10; row++) {

            List<GridSeat> rowSeats = new ArrayList<>();

            for (char col = 'A'; col <= 'D'; col++) {

                String seatNumber = row + "" + col;

                Seat seat = seats.stream()
                        .filter(s -> s.getSeatNumber().equals(seatNumber))
                        .findFirst()
                        .orElseThrow();

                boolean available =
                        seat.isAvailable(start, end, isForward);

                rowSeats.add(
                        new GridSeat(
                                seatNumber,
                                available ? "AVAILABLE" : "BOOKED"
                        )
                );
            }

            grid.add(rowSeats);
        }

        return new GridSeatMapResponse(
                origin,
                destination,
                travelDate,
                grid
        );
    }

    private int index(String location) {

        return switch(location) {

            case "A" -> 0;
            case "B" -> 1;
            case "C" -> 2;
            case "D" -> 3;
            default -> throw new RuntimeException("Invalid");
        };
    }
}
