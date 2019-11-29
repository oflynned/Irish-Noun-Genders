package com.syzible.irishnoungenders.common.common;

public enum FeatureFlag {
    ENABLE_SHOWCASE_SCREENSHOT, EARN_ACHIEVEMENTS, VIEW_ACHIEVEMENTS, SHOW_LEADERBOARDS, SHOW_TUTORIAL_SANDBOX;

    public boolean isEnabled() {
        switch (this) {
            case SHOW_TUTORIAL_SANDBOX:
                return true;
            // TODO set this via some properties file so that automatic screenshots can be taken
            case ENABLE_SHOWCASE_SCREENSHOT:
            case EARN_ACHIEVEMENTS:
            case VIEW_ACHIEVEMENTS:
            case SHOW_LEADERBOARDS:
            default:
                return false;
        }
    }
}
