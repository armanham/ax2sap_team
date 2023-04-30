package com.bdg.airport_management_system.model;

import com.bdg.airport_management_system.model.common.BaseMod;

import java.sql.Timestamp;
import java.util.Objects;

import static com.bdg.airport_management_system.validator.Validator.checkNull;
import static com.bdg.airport_management_system.validator.Validator.validateString;


public class PassInTripMod extends BaseMod {

    private TripMod trip;
    private PassengerMod passenger;
    private String place;
    private Timestamp time;

    public PassInTripMod(
            final TripMod trip,
            final PassengerMod passenger,
            final String place,
            final Timestamp time) {
        setTrip(trip);
        setPassenger(passenger);
        setPlace(place);
        setTime(time);
    }

    public PassInTripMod() {
    }

    public TripMod getTrip() {
        return trip;
    }

    public void setTrip(final TripMod trip) {
        checkNull(trip);
        this.trip = trip;
    }

    public PassengerMod getPassenger() {
        return passenger;
    }

    public void setPassenger(final PassengerMod passenger) {
        checkNull(passenger);
        this.passenger = passenger;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(final String place) {
        validateString(place);
        this.place = place;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(final Timestamp time) {
        checkNull(time);
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PassInTripMod that = (PassInTripMod) o;
        return Objects.equals(trip, that.trip) && Objects.equals(passenger, that.passenger) && Objects.equals(place, that.place);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trip, passenger, place);
    }

    @Override
    public String toString() {
        return "PassInTripMod{" +
                "id=" + getId() +
                ", trip=" + trip +
                ", passenger=" + passenger +
                ", time=" + time +
                ", place='" + place + '\'' +
                '}';
    }
}