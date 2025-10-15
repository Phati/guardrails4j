package com.github.phati.guardrails4j.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuardException extends RuntimeException {

    private String guardName;
    private String guardResponse;

    public GuardException(String guardName, String guardResponse, String message) {
        super(message);
        this.guardName = guardName;
        this.guardResponse = guardResponse;
    }

    public GuardException(String message) {
        super(message);
    }
}
