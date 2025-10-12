package com.github.phati.guardrails4j.model;

public enum GuardDecision {
    ALLOW,      // allow through
    REJECT,     // politely reject (e.g., competitor question)
    BLOCK,      // harmful/unsafe => hard block
    REVIEW      // flag for human review or fallback flow
}