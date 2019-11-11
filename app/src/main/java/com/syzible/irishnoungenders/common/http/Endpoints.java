package com.syzible.irishnoungenders.common.http;

public class Endpoints {
    private static final String STEM_URL = Development.isDebugMode() ? "http://10.0.2.2:3001/api" : "https://irish-nouns.syzible.com/api";
//    private static final String STEM_URL = "https://t1-exercise.herokuapp.com";

    public static final String REPORT_EXPERIMENT = STEM_URL + "/experiments";
}
