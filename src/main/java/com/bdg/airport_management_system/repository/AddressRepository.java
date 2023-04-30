package com.bdg.airport_management_system.repository;

import com.bdg.airport_management_system.model.AddressMod;
import com.bdg.airport_management_system.repository.common.CommonRepository;

public interface AddressRepository extends CommonRepository<AddressMod> {

    int getId(AddressMod addressMod);

    boolean exists(AddressMod addressMod);

    boolean updateBy(int id, String newCountry, String newCity);
}