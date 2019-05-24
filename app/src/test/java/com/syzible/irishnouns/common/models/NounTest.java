package com.syzible.irishnouns.common.models;

import com.google.gson.Gson;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class NounTest {
    @Test
    public void shouldHaveTranslationListWithoutComma() {
        Noun noun = new Noun();
        noun.setTranslations(Collections.singletonList("first"));
        Assert.assertEquals("first", noun.getTranslations());
    }

    @Test
    public void shouldHaveTranslationListCommaSeparated() {
        Noun noun = new Noun();
        noun.setTranslations(Arrays.asList("first", "second"));
        Assert.assertEquals("first, second", noun.getTranslations());
    }

    private Noun noun;

    @Before
    public void setup() {
        HashMap<String, Object> payload = new HashMap<>();
        payload.put("ga", "title");
        payload.put("en", new String[]{"en"});
        payload.put("declension", 1);
        payload.put("gender", "masculine");

        String apiResponse = new Gson().toJson(payload);
        noun = new Gson().fromJson(apiResponse, Noun.class);
    }

    @Test
    public void shouldHaveTitle() {
        Assert.assertEquals("title", noun.getTitle());
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