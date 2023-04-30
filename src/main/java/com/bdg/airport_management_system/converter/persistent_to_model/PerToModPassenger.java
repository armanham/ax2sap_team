package com.bdg.airport_management_system.converter.persistent_to_model;

import com.bdg.airport_management_system.converter.persistent_to_model.common.PerToMod;
import com.bdg.airport_management_system.model.PassengerMod;
import com.bdg.airport_management_system.persistent.PassengerPer;
import com.bdg.airport_management_system.validator.Validator;

public class PerToModPassenger extends PerToMod<PassengerPer, PassengerMod> {

    private final static PerToModAddress PER_TO_MOD_ADDRESS = new PerToModAddress();

    @Override
    public PassengerMod getModelFrom(PassengerPer persistent) {
        Validator.checkNull(persistent);

        PassengerMod model = new PassengerMod();
        model.setId(persistent.getId());
        model.setName(persistent.getName());
        model.setPhone(persistent.getPhone());
        model.setAddress(PER_TO_MOD_ADDRESS.getModelFrom(persistent.getAddress()));

        return model;
    }
}