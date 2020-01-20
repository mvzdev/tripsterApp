package com.mobileapp.tripster;

import java.util.Date;

public class Connection {

    private int id;
    private Date departure;
    private Date arrival;

    public Connection(int id, Date departure, Date arrival) {
        this.id = id;
        this.departure = departure;
        this.arrival = arrival;
    }

    public int getId() {
        return id;
    }


    public Date getDeparture() {
        return departure;
    }

    public Date getArrival() {
        return arrival;
    }
}
