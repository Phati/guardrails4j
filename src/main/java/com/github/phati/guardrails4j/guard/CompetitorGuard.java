package com.github.phati.guardrails4j.guard;


import com.github.phati.guardrails4j.model.GuardResponse;
import com.github.phati.guardrails4j.model.UserQuery;

public interface CompetitorGuard extends Guard {
    default GuardResponse checkForCompetitorsMention(UserQuery userQuery) {
        return evaluate(userQuery);
    }
}
