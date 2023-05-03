package com.bdg.airport_management_system.repository.common;

import com.bdg.airport_management_system.model.AddressMod;

import java.util.Set;

public interface CommonRepository<T> {

    T getBy(int id);

    int getId(T item);

    Set<T> getAll();

    Set<T> get(int offset, int perPage, String sort);

    T save(T item);

    boolean deleteBy(int id);
}