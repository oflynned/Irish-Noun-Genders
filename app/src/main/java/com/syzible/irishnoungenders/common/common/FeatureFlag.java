package com.syzible.irishnoungenders.common.common;

public enum FeatureFlag {
    EARN_ACHIEVEMENTS, VIEW_ACHIEVEMENTS, SHOW_LEADERBOARDS, SHOW_TUTORIAL_SANDBOX;

    public boolean isEnabled() {
        switch (this) {
            case EARN_ACHIEVEMENTS:
            case VIEW_ACHIEVEMENTS:
            case SHOW_LEADERBOARDS:
            case SHOW_TUTORIAL_SANDBOX:
            default:
                return false;
        }
    }
}
