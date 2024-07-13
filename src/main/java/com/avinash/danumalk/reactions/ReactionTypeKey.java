package com.avinash.danumalk.reactions;

public enum ReactionTypeKey {
    LIKE("like"),
    LOVE("love");


    private final String key;

    ReactionTypeKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
