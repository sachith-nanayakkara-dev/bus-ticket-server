package org.sachith.dispatcher;

import org.sachith.controller.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Central registry for mapping RouteKey to Controller instances.
 * Defines available routes and their corresponding handlers.
 */
public class RouteRegistry {

    public static Map<RouteKey, Controller> routes() {

        Map<RouteKey, Controller> routes = new HashMap<>();

        routes.put(RouteKey.of("POST", "/availability"), new AvailabilityController());
        routes.put(RouteKey.of("POST", "/reserve"), new ReservationController());

        routes.put(RouteKey.of("GET", "/seat-map/grid"), new SeatGridMapController());

        return routes;
    }
}
