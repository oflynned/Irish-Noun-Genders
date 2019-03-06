package com.syzible.irishnoungenders.data.repository;

import com.syzible.irishnoungenders.data.entity.NounEntity;
import com.syzible.irishnoungenders.domain.model.Noun;

import javax.inject.Inject;

public class NounToNounEntityMapper extends com.syzible.irishnoungenders.data.repository.datasource.mapper.Mapper<Noun, NounEntity> {

    @Inject
    public NounToNounEntityMapper() {
    }

    @Override
    public NounEntity map(Noun value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Noun reverseMap(NounEntity value) {
        return new Noun(value.getIrishForm(), value.getEnglishForms(), value.getGender(), value.getDeclension());
    }
}
