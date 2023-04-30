package com.bdg.airport_management_system.service;

import com.bdg.airport_management_system.model.PassInTripMod;
import com.bdg.airport_management_system.repository.PassInTripRepository;

import java.util.Set;

public class PassInTripService implements PassInTripRepository {

    @Override
    public PassInTripMod getBy(int id) {
        return null;
    }

    @Override
    public Set<PassInTripMod> getAll() {
        return null;
    }

    @Override
    public Set<PassInTripMod> get(int offset, int perPage, String sort) {
        return null;
    }

    @Override
    public PassInTripMod save(PassInTripMod item) {
        return null;
    }

    @Override
    public boolean deleteBy(int id) {
        return false;
    }
}
