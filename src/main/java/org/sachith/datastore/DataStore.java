package org.sachith.datastore;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class for holding in-memory reservation data.
 */
public class DataStore {

    /**
     * In-memory data structure that holds all reservation objects during the
     * server's runtime.
     * Each reservation is stored by its reservation ID.
     * All reservations exist only in memory and are lost when the server restarts.
     */
    public static final ConcurrentHashMap<String, Object> reservations = new ConcurrentHashMap<>();

    // Add a counter for reservation numbers
    public static int reservationCounter = 1;

}
