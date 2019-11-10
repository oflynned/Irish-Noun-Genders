package com.syzible.irishnoungenders.common.firebase;

public enum FeatureFlag {
    EARN_ACHIEVEMENTS, VIEW_ACHIEVEMENTS;

    public boolean isEnabled() {
        switch (this) {
            case EARN_ACHIEVEMENTS:
            case VIEW_ACHIEVEMENTS:
            default:
                return false;
        }
    }
}
