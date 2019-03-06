package com.syzible.irishnoungenders.data.repository.datasource.mapper;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.syzible.irishnoungenders.data.entity.NounEntity;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

public class NounEntityJsonMapper {
    private final Gson gson;

    @Inject
    public NounEntityJsonMapper() {
        gson = new Gson();
    }

    public List<NounEntity> transformNounEntity(String nounJsonResponse) throws JsonSyntaxException {
        List<NounEntity> nounEntityList;
        try {
            Type typeNounEntityList = new TypeToken<List<NounEntity>>() {
            }.getType();
            nounEntityList = this.gson.fromJson(nounJsonResponse, typeNounEntityList);
            return nounEntityList;
        } catch (JsonSyntaxException exception) {
            exception.printStackTrace();
            throw exception;
        }
    }
}
