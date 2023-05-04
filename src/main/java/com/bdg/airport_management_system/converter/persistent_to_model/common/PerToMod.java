package com.bdg.airport_management_system.converter.persistent_to_model.common;

import com.bdg.airport_management_system.model.common.BaseMod;
import com.bdg.airport_management_system.persistent.common.BaseEntity;
import com.bdg.airport_management_system.validator.Validator;

import java.util.Collection;
import java.util.stream.Collectors;

public abstract class PerToMod<P extends BaseEntity, M extends BaseMod> {

    public PerToMod() {
    }

    public abstract M getModelFrom(P persistent);

    public Collection<M> getModelListFrom(Collection<P> persistentList) {
        Validator.checkNull(persistentList);

        return persistentList
                .stream()
                .map(this::getModelFrom)
                .collect(Collectors.toSet());
    }
}