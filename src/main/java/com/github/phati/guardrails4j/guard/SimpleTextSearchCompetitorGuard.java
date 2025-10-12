package com.github.phati.guardrails4j.guard;

import com.github.phati.guardrails4j.configuration.GuardConfigProperties;
import com.github.phati.guardrails4j.model.GuardDecision;
import com.github.phati.guardrails4j.model.GuardResponse;
import com.github.phati.guardrails4j.model.UserQuery;

public class SimpleTextSearchCompetitorGuard implements CompetitorGuard {

    private final Integer order;
    private final GuardConfigProperties.CompetitorGuard competitorGuard;

    public SimpleTextSearchCompetitorGuard(GuardConfigProperties.CompetitorGuard competitorGuard) {
        this.order = competitorGuard.getSimpleTextBased().getOrder();
        this.competitorGuard = competitorGuard;
    }

    @Override
    public GuardResponse evaluate(UserQuery userQuery) {
        for (String competitor : competitorGuard.getCompetitorList()) {
            if (userQuery.getQuery().toLowerCase().contains(competitor.toLowerCase())) {
                return GuardResponse.builder()
                        .decision(GuardDecision.BLOCK)
                        .message(competitorGuard.getDefaultResponse())
                        .guardName("SimpleTextSearchCompetitorGuard")
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
