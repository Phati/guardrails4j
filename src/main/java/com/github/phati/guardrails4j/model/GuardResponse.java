package com.github.phati.guardrails4j.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class GuardResponse {

    private final GuardDecision decision;

    private String message;

    private String guardName;

}
