package com.bdg.airport_management_system;

import com.bdg.airport_management_system.hibernate.HibernateUtil;
import com.bdg.airport_management_system.service.AddressService;

public class Main {

    public static void main(String[] args) {

//        Inserter inserter = new Inserter();
//
//        inserter.insertCompanyTable();
//        inserter.insertAddressTable();
//        inserter.insertPassengerTable();
//        inserter.insertTripTable();
//        inserter.insertPassInTripTable();

        AddressService addressService = new AddressService();

//        PassengerService passengerService = new PassengerService();
//        passengerService.save(new PassengerMod("Sona", "000000", new AddressMod("Armenia", "Yerevan")));
//        //addressService.save(new AddressMod("Armenia", "Yerevan"));
//        //System.out.println(addressService.getBy(1));
        System.out.println(addressService.getAll());
        HibernateUtil.shutdown();

    }
}