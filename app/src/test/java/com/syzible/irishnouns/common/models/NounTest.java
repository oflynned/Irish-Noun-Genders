package com.syzible.irishnouns.common.models;

import com.google.gson.Gson;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;

public class NounTest {
    private Noun noun;

    @Before
    public void before() {
        HashMap<String, Object> fixture = new HashMap<>();
        fixture.put("declension", 1);
        fixture.put("gender", "masculine");
        fixture.put("ga", "ga");
        fixture.put("en", new String[]{"en"});
        String apiResponse = new Gson().toJson(fixture);
        noun = new Gson().fromJson(apiResponse, Noun.class);
    }

    @Test
    public void shouldHaveTitle() {
        Assert.assertEquals("ga", noun.getTitle());
    }

    @Test
    public void shouldHaveTranslationList() {
        Assert.assertEquals(Collections.singletonList("en"), noun.getTranslations());
    }

    @Test
    public void shouldHaveDeclension() {
        Assert.assertEquals(1, noun.getDeclension());
    }

    @Test
    public void shouldHaveGender() {
        Assert.assertEquals(Noun.Gender.MASCULINE, noun.getGender());
    }
}