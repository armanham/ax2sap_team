package com.bdg.airport_management_system.converter.model_to_persistent;

import com.bdg.airport_management_system.converter.model_to_persistent.common.ModToPer;
import com.bdg.airport_management_system.model.TripMod;
import com.bdg.airport_management_system.persistent.TripPer;
import com.bdg.airport_management_system.validator.Validator;

public class ModToPerTrip extends ModToPer<TripMod, TripPer> {

    private static final ModToPerCompany MOD_TO_PER_COMPANY = new ModToPerCompany();

    @Override
    public TripPer getPersistentFrom(TripMod model) {
        Validator.checkNull(model);

        TripPer persistent = new TripPer();
        persistent.setCompany(MOD_TO_PER_COMPANY.getPersistentFrom(model.getCompany()));
        persistent.setAirplane(model.getAirplane());
        persistent.setTownFrom(model.getTownFrom());
        persistent.setTownTo(model.getTownTo());
        persistent.setTimeIn(model.getTimeIn());
        persistent.setTimeOut(model.getTimeOut());

        return persistent;
    }
}