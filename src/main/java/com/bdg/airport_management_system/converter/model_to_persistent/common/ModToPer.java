package com.bdg.airport_management_system.converter.model_to_persistent.common;

import com.bdg.airport_management_system.model.common.BaseMod;
import com.bdg.airport_management_system.persistent.common.BaseEntity;
import com.bdg.airport_management_system.validator.Validator;

import java.util.Collection;
import java.util.stream.Collectors;

public abstract class ModToPer<M extends BaseMod, P extends BaseEntity> {

    public ModToPer() {
    }

    public abstract P getPersistentFrom(M model);

    public Collection<P> getPersistentListFrom(Collection<M> modelList) {
        Validator.checkNull(modelList);

        return modelList
                .stream()
                .map(this::getPersistentFrom)
                .collect(Collectors.toSet());
    }
}