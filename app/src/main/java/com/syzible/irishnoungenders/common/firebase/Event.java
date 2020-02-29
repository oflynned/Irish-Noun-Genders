package com.syzible.irishnoungenders.common.firebase;

public enum Event {
    COMPLETE_FIRST_RUN,
    START_SIGN_IN, COMPLETE_SIGN_IN, SILENT_SIGN_IN, SIGN_OUT,
    START_GAME_MODE,
    SHOW_NEW_WORD, MAKE_GUESS, DECK_FINISHED, CHANGE_DOMAIN, LEAVE_GAME
}
