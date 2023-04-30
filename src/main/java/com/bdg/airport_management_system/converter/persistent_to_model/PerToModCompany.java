package com.bdg.airport_management_system.converter.persistent_to_model;

import com.bdg.airport_management_system.converter.persistent_to_model.common.PerToMod;
import com.bdg.airport_management_system.model.CompanyMod;
import com.bdg.airport_management_system.persistent.CompanyPer;
import com.bdg.airport_management_system.validator.Validator;

public class PerToModCompany extends PerToMod<CompanyPer, CompanyMod> {

    @Override
    public CompanyMod getModelFrom(CompanyPer persistent) {
        Validator.checkNull(persistent);

        CompanyMod model = new CompanyMod();
        model.setId(persistent.getId());
        model.setName(persistent.getName());
        model.setFoundDate(persistent.getFoundDate());

        return model;
    }
}