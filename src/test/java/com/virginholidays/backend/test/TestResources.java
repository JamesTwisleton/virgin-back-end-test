package com.virginholidays.backend.test;

import com.virginholidays.backend.test.api.Flight;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public class TestResources {

    Flight flight0 = new Flight(
            LocalTime.of(01, 02),
            "London",
            "LDN",
            "1234",
            List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
    Flight flight1 = new Flight(
            LocalTime.of(04, 59),
            "London",
            "LDN",
            "5678",
            List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
    public List testFlights = List.of(flight0, flight1);
}
