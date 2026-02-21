package org.sachith.repository;

import org.sachith.model.Trip;

public interface TripRepository {

    Trip findOrCreate(String date, boolean forward);
}
