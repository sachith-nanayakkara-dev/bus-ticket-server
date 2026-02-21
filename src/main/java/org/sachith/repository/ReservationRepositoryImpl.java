package org.sachith.repository;

import org.sachith.datastore.DataStore;
import org.sachith.model.Reservation;

public class ReservationRepositoryImpl implements ReservationRepository{

    @Override
    public void save(Reservation reservation) {

        DataStore.reservations.put(
                reservation.getReservationId(),
                reservation);
    }

    @Override
    public synchronized String generateReservationId(
            String origin,
            String destination) {

        String prefix = origin + destination;

        return String.format("%s%04d",
                prefix.toUpperCase(),
                DataStore.reservationCounter++);
    }
}
