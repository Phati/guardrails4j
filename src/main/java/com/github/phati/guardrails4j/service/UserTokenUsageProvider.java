package com.github.phati.guardrails4j.service;

import com.github.phati.guardrails4j.model.UserTokenUsage;

public class UserTokenUsageProvider {

    public UserTokenUsage getUserTokenUsage(String userId) {
        return new UserTokenUsage(userId, 0);
    }

}
