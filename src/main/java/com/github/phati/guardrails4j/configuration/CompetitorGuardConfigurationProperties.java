package com.github.phati.guardrails4j.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "guard.competitors")
public class CompetitorGuardConfigurationProperties {

    private StaticTextBased staticTextBased;

    private LlmBased llmBased;

    @Data
    public static class StaticTextBased {
        private boolean enabled;
        private int order;
        private String[] list;
    }

    @Data
    public static class LlmBased {
        private boolean enabled;
        private int order;
    }

}
