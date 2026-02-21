package org.sachith.repository;

import org.sachith.model.Reservation;

public interface ReservationRepository {
    void save(Reservation reservation);
    String generateReservationId(String origin, String destination);

}
