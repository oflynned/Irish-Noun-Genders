package com.syzible.irishnoungenders.common.firebase;

public interface GameServices {
    void signInSilently();

    void signInExplicitly();

    boolean isSignedIn();
}
