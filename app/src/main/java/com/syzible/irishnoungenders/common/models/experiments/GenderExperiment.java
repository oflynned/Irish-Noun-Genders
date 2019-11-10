package com.syzible.irishnoungenders.common.models.firebase;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class GenderExperiment {
    public String userId, posedNoun, answerGiven, actualAnswer;

    public GenderExperiment() {

    }

    public GenderExperiment(String userId, String posedNoun, String answerGiven, String actualAnswer) {
        this.userId = userId;
        this.posedNoun = posedNoun;
        this.answerGiven = answerGiven;
        this.actualAnswer = actualAnswer;
    }
}
