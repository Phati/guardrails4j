package com.github.phati.guardrails4j.model;

import lombok.Data;

@Data
public class AgentIntent {

    private String agent;

    private String intent;

    private int order;

}
