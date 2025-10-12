package com.github.phati.guardrails4j.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GuardRailsEngineResponse {

    List<GuardResponse> passedGuardResponses;

    GuardResponse faildGuardResponse;

    boolean allow;

}
