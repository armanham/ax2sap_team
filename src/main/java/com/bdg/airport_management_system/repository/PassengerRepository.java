package com.bdg.airport_management_system.repository;

import com.bdg.airport_management_system.model.AddressMod;
import com.bdg.airport_management_system.model.PassengerMod;
import com.bdg.airport_management_system.repository.common.CommonRepository;

import java.util.List;

public interface PassengerRepository extends CommonRepository<PassengerMod> {

    List<PassengerMod> getAllOf(int tripId);

    boolean registerTrip(int tripId, PassengerMod passenger, String place);

    boolean cancelTrip(int tripId, int passengerId, String place);

    boolean updateBy(int id, String newName, String newPhone, AddressMod newAddress);
}