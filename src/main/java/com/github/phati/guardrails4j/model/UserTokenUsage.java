package com.github.phati.guardrails4j.model;

import java.time.LocalDate;

public class UserTokenUsage {

    private String userId;

    private int tokenCount;

    private LocalDate date;

    public UserTokenUsage(String userId, int tokenCount) {
        this.userId = userId;
        this.tokenCount = tokenCount;
    }

}
