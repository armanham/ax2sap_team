package com.bdg.airport_management_system.repository;

import com.bdg.airport_management_system.model.TripMod;
import com.bdg.airport_management_system.repository.common.CommonRepository;

import java.sql.Timestamp;
import java.util.Set;

public interface TripRepository extends CommonRepository<TripMod> {

    Set<TripMod> getAllFrom(String city);

    Set<TripMod> getAllTo(String city);

    boolean updateBy(int idToUpdate,
                     String newAirplane, String newTownFrom, String newTownTo,
                     Timestamp newTimeOut,
                     Timestamp newTimeIn);
}