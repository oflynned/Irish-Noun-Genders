package com.syzible.irishnoungenders.domain.model;

import java.util.ArrayList;
import java.util.List;

import static com.syzible.irishnoungenders.data.entity.NounEntity.Gender;

/**
 * Created by ed on 15/08/2017.
 */

public class Noun {
    private Gender gender;
    private int declension;
    private String irishForm;
    private List<String> englishForms;

    public Noun(String irishForm, List<String> englishForms, Gender gender, int declension) {
        this.gender = gender;
        this.irishForm = irishForm;
        this.englishForms = englishForms;
        this.declension = declension;
    }

    public Gender getGender() {
        return gender;
    }

    public int getDeclension() {
        return declension;
    }

    public String getIrishForm() {
        return irishForm;
    }

    public List<String> getEnglishForms() {
        return englishForms;
    }

    public String getEnglishTranslation() {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < englishForms.size(); i++) {
            output.append(englishForms.get(i));
            // second last noun should be the limit for adding commas
            if (i < englishForms.size() - 1)
                output.append(", ");
        }

        return output.toString();
    }
}
