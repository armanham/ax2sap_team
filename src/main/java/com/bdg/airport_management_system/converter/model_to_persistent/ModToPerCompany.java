package com.bdg.airport_management_system.converter.model_to_persistent;

import com.bdg.airport_management_system.converter.model_to_persistent.common.ModToPer;
import com.bdg.airport_management_system.model.CompanyMod;
import com.bdg.airport_management_system.persistent.CompanyPer;
import com.bdg.airport_management_system.validator.Validator;

public class ModToPerCompany extends ModToPer<CompanyMod, CompanyPer> {

    @Override
    public CompanyPer getPersistentFrom(CompanyMod model) {
        Validator.checkNull(model);

        CompanyPer persistent = new CompanyPer();
        persistent.setName(model.getName());
        persistent.setFoundDate(model.getFoundDate());

        return persistent;
    }
}