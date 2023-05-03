package com.bdg.airport_management_system.repository;

import com.bdg.airport_management_system.model.AddressMod;
import com.bdg.airport_management_system.repository.common.CommonRepository;

public interface AddressRepository extends CommonRepository<AddressMod> {

    boolean updateBy(int idToUpdate, String newCountry, String newCity);
}