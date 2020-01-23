package com.syzible.irishnoungenders.screens.options.rulequickstart;

import com.syzible.irishnoungenders.common.models.Noun;

import java.util.List;

public class Explanation {

    private Noun.Gender gender;
    private String hint;
    private List<Noun> exampleNouns;

    Explanation(Noun.Gender gender, String hint, List<Noun> exampleNouns) {
        this.gender = gender;
        this.hint = hint;
        this.exampleNouns = exampleNouns;
    }

    public Noun.Gender getGender() {
        return gender;
    }

    public String getHint() {
        return hint;
    }

    public List<Noun> getExampleNouns() {
        return exampleNouns;
    }
}
