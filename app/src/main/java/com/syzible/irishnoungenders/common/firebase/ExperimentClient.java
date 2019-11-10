package com.syzible.irishnoungenders.common.firebase;

public interface ExperimentClient {
    void syncGenderExperiment(String posedNoun, String answerGiven, String actualAnswer);
}
