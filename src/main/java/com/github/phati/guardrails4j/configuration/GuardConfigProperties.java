package com.github.phati.guardrails4j.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "guard")
public class GuardConfigProperties {

    private CompetitorGuard competitors;

    @Data
    public static class CompetitorGuard {

        private String defaultResponse = "I'm sorry, I don’t have enough information to answer that question at the moment.";

        private String[] competitorList;

        private String ourCompanyName;

        private SimpleTextBased simpleTextBased;

        private LlmBased llmBased;

        private VectorBased vectorBased;

        @Data
        public static class SimpleTextBased {
            private boolean enabled;
            private int order;
        }

        @Data
        public static class LlmBased {
            private boolean enabled;
            private int order;
            private String chatClientBeanName;
        }

        @Data
        public static class VectorBased {
            private boolean enabled;
            private int order;
        }

    }


}
