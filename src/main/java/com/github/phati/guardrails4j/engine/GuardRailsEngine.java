package com.github.phati.guardrails4j.engine;


import com.github.phati.guardrails4j.guard.Guard;
import com.github.phati.guardrails4j.model.GuardDecision;
import com.github.phati.guardrails4j.model.GuardRailsEngineResponse;
import com.github.phati.guardrails4j.model.GuardResponse;
import com.github.phati.guardrails4j.model.UserQuery;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Log4j2
public class GuardRailsEngine {

    private final List<Guard> guards;

    public GuardRailsEngine(List<Guard> guards) {
        this.guards = guards;
        this.guards.sort(Comparator.comparing(Guard::getOrder));
    }

    public GuardRailsEngineResponse evaluateAll(UserQuery userQuery) {
        List<GuardResponse> passedGuards = new ArrayList<>();
        for (Guard guard : guards) {
            GuardResponse guardResponse = guard.evaluate(userQuery);
            log.debug("Guard: {} evaluated with response: {} in order :{}", guard.getClass().getSimpleName(), guardResponse, guard.getOrder());
            if (guardResponse.getDecision().equals(GuardDecision.BLOCK)) {
                return GuardRailsEngineResponse.builder()
                        .allow(false)
                        .passedGuardResponses(passedGuards)
                        .faildGuardResponse(guardResponse)
                        .build();
            }
            passedGuards.add(guardResponse);
        }
        return GuardRailsEngineResponse.builder()
                .allow(true)
                .passedGuardResponses(passedGuards)
                .build();
    }

}
