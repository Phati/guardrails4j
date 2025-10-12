package com.github.phati.guardrails4j.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CompetitorGuardLLMResponse {
    private String action;
    private String reason;
}
