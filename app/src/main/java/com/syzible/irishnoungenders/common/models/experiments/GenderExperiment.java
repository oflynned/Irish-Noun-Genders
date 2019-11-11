package com.syzible.irishnoungenders.common.models.experiments;

public class GenderExperiment {
    private String userId, posedNoun, answerGiven, actualAnswer;

    public GenderExperiment(String userId, String posedNoun, String answerGiven, String actualAnswer) {
        this.userId = userId;
        this.posedNoun = posedNoun;
        this.answerGiven = answerGiven;
        this.actualAnswer = actualAnswer;
    }

    public String getUserId() {
        return userId;
    }

    public String getPosedNoun() {
        return posedNoun;
    }

    public String getAnswerGiven() {
        return answerGiven;
    }

    public String getActualAnswer() {
        return actualAnswer;
    }
}
