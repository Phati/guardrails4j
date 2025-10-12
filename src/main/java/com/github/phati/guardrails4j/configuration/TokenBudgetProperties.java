package com.github.phati.guardrails4j.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "guardrails.tokens")
public class TokenBudgetProperties {

    private int perRequestMax = 4000;

    private int perDayMax = 20000;

}
