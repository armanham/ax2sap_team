package com.bdg.airport_management_system.converter.persistent_to_model;

import com.bdg.airport_management_system.converter.persistent_to_model.common.PerToMod;
import com.bdg.airport_management_system.model.PassInTripMod;
import com.bdg.airport_management_system.persistent.PassInTripPer;
import com.bdg.airport_management_system.validator.Validator;

public class PerToModPassInTrip extends PerToMod<PassInTripPer, PassInTripMod> {

    private static final PerToModTrip PER_TO_MOD_TRIP = new PerToModTrip();
    private static final PerToModPassenger PER_TO_MOD_PASSENGER = new PerToModPassenger();

    @Override
    public PassInTripMod getModelFrom(PassInTripPer persistent) {
        Validator.checkNull(persistent);

        PassInTripMod model = new PassInTripMod();
        model.setId(persistent.getId());
        model.setPassenger(PER_TO_MOD_PASSENGER.getModelFrom(persistent.getPassenger()));
        model.setTrip(PER_TO_MOD_TRIP.getModelFrom(persistent.getTrip()));
        model.setPlace(persistent.getPlace());
        model.setTime(persistent.getTime());

        return model;
    }
}