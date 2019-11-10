package com.syzible.irishnoungenders.common.http;

import com.google.firebase.database.DatabaseReference;
import com.syzible.irishnoungenders.common.models.experiments.GenderExperiment;

public class FirebaseService {
    private static final String GENDER_EXPERIMENT_TABLE = "gender_guesses";

    public static void syncExperiment(DatabaseReference reference, GenderExperiment experiment) {
        reference.child(GENDER_EXPERIMENT_TABLE).setValue(experiment);
    }
}
