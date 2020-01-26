package com.mobileapp.tripster;

import java.util.Date;
import java.util.Random;

public class Connection {

    private int id;
    private final Date departure;
    private final String departureStationName;
    private final Date arrival;
    private final String arrivalStationName;

    public Connection( Date departure, String departureLocation, Date arrival, String arrivalStationName) {
        this.id = new Random().nextInt();
        this.departure = departure;
        this.departureStationName = departureLocation;
        this.arrival = arrival;
        this.arrivalStationName = arrivalStationName;
    }

    public int getId() {
        return id;
    }

    public Date getDeparture() {
        return departure;
    }

    public String getDepartureStationName() {
        return departureStationName;
    }

    public Date getArrival() {
        return arrival;
    }

    public String getArrivalStationName() {
        return arrivalStationName;
    }
}
