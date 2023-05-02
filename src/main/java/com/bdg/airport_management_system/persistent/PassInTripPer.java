package com.bdg.airport_management_system.persistent;

import com.bdg.airport_management_system.persistent.common.BaseEntity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(
        name = "pass_in_trip",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"trip_id", "passenger_id", "place"})}
)
public class PassInTripPer extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "trip_id", referencedColumnName = "id", nullable = false)
    private TripPer trip;

    @ManyToOne
    @JoinColumn(name = "passenger_id", referencedColumnName = "id", nullable = false)
    private PassengerPer passenger;

    @Column(updatable = false, nullable = false)
    private Timestamp time;

    @Column(nullable = false, updatable = false, length = 3)
    private String place;


    public PassInTripPer() {
    }


    public TripPer getTrip() {
        return trip;
    }

    public void setTrip(TripPer trip) {
        this.trip = trip;
    }

    public PassengerPer getPassenger() {
        return passenger;
    }

    public void setPassenger(PassengerPer passenger) {
        this.passenger = passenger;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}