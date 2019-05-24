package com.syzible.irishnoungenders.common.models;

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
        Noun noun = new Noun(1, "title", Collections.singletonList("first"), Noun.Gender.MASCULINE);
        Assert.assertEquals("first", noun.getTranslations());
    }

    @Test
    public void shouldHaveTranslationListCommaSeparated() {
        Noun noun = new Noun(1, "title", Arrays.asList("first", "second"), Noun.Gender.MASCULINE);
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

    @Test
    public void shouldFormatToString() {
        Assert.assertEquals("Noun{declension=1, title='title', translations='en', gender='MASCULINE'}", noun.toString());
    }
}