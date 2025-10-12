package com.github.phati.guardrails4j.guard;

import com.github.phati.guardrails4j.configuration.GuardConfigProperties;
import com.github.phati.guardrails4j.model.GuardDecision;
import com.github.phati.guardrails4j.model.GuardResponse;
import com.github.phati.guardrails4j.model.UserQuery;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class SimpleTextSearchCompetitorGuard implements CompetitorGuard {

    private final Integer order;
    private final GuardConfigProperties.CompetitorGuard competitorGuard;

    public SimpleTextSearchCompetitorGuard(GuardConfigProperties.CompetitorGuard competitorGuard) {
        this.order = competitorGuard.getSimpleTextBased().getOrder();
        this.competitorGuard = competitorGuard;
    }

    @Override
    public GuardResponse evaluate(UserQuery userQuery) {
        log.debug("SimpleTextSearchCompetitorGuard: Evaluating user query: {}", userQuery.getQuery());
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
