package com.bdg.airport_management_system.converter.model_to_persistent;

import com.bdg.airport_management_system.converter.model_to_persistent.common.ModToPer;
import com.bdg.airport_management_system.model.AddressMod;
import com.bdg.airport_management_system.persistent.AddressPer;
import com.bdg.airport_management_system.validator.Validator;

public class ModToPerAddress extends ModToPer<AddressMod, AddressPer> {

    @Override
    public AddressPer getPersistentFrom(AddressMod model) {
        Validator.checkNull(model);

        AddressPer persistent = new AddressPer();
        persistent.setCountry(model.getCountry());
        persistent.setCity(model.getCity());

        return persistent;
    }
}