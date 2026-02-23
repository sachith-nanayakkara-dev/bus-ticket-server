package org.sachith.service;

import org.sachith.dto.JourneyInfo;
import org.sachith.util.LocationUtils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class ScheduleServiceImpl implements ScheduleService {

        private static final Map<String, LocalTime> FORWARD_SCHEDULE = Map.of(
                        "A", LocalTime.of(8, 0),
                        "B", LocalTime.of(8, 30),
                        "C", LocalTime.of(9, 0),
                        "D", LocalTime.of(9, 30));

        private static final Map<String, LocalTime> RETURN_SCHEDULE = Map.of(
                        "D", LocalTime.of(17, 0),
                        "C", LocalTime.of(17, 30),
                        "B", LocalTime.of(18, 0),
                        "A", LocalTime.of(18, 30));

        @Override
        public JourneyInfo calculateJourney(
                        String origin,
                        String destination) {

                Map<String, LocalTime> schedule = determineSchedule(origin, destination);

                LocalTime departure = schedule.get(origin);

                LocalTime arrival = schedule.get(destination);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

                return new JourneyInfo(
                                origin,
                                destination,
                                departure.format(formatter),
                                arrival.format(formatter));
        }

        private Map<String, LocalTime> determineSchedule(
                        String origin,
                        String destination) {

                int originIndex = LocationUtils.index(origin);
                int destinationIndex = LocationUtils.index(destination);

                if (originIndex < destinationIndex) {

                        return FORWARD_SCHEDULE;
                } else {

                        return RETURN_SCHEDULE;
                }
        }

}
