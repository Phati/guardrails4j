package com.github.phati.guardrails4j.guard;

import com.github.phati.guardrails4j.configuration.CompetitorGuardConfigurationProperties;
import com.github.phati.guardrails4j.model.GuardDecision;
import com.github.phati.guardrails4j.model.GuardResponse;
import com.github.phati.guardrails4j.model.UserQuery;

public class StaticTextSearchCompetitorGuard implements CompetitorGuard {

    private final Integer order;
    private final CompetitorGuardConfigurationProperties.StaticTextBased staticTextBased;

    public StaticTextSearchCompetitorGuard(CompetitorGuardConfigurationProperties.StaticTextBased staticTextBased) {
        this.order = staticTextBased.getOrder();
        this.staticTextBased = staticTextBased;
    }

    @Override
    public GuardResponse evaluate(UserQuery userQuery) {
        for (String competitor : staticTextBased.getList()) {
            if (userQuery.getQuery().toLowerCase().contains(competitor.toLowerCase())) {
                return GuardResponse.builder()
                        .decision(GuardDecision.BLOCK)
                        .message("I cannot assist with this request, please avoid mentioning competitors")
                        .build();
            }
        }

        return GuardResponse.builder()
                .decision(GuardDecision.ALLOW)
                .message("No competitors mentioned")
                .build();
    }

    @Override
    public Integer getOrder() {
        return order;
    }

}
