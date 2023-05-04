package com.bdg.airport_management_system.repository;

import com.bdg.airport_management_system.model.CompanyMod;
import com.bdg.airport_management_system.repository.common.CommonRepository;

public interface CompanyRepository extends CommonRepository<CompanyMod> {

    boolean updateBy(int idToUpdate, String newName);
}