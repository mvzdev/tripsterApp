package com.mobileapp.tripster;

import java.util.Date;
import java.util.Random;

public class Connection {

    private int id;
    private Date departure;
    private Date arrival;

    public Connection(Date departure, Date arrival) {
        this.id = new Random().nextInt();
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
