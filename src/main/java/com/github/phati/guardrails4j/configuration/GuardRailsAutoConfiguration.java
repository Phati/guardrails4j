package com.github.phati.guardrails4j.configuration;

import com.github.phati.guardrails4j.engine.GuardRailsEngine;
import com.github.phati.guardrails4j.guard.Guard;
import com.github.phati.guardrails4j.guard.StaticTextSearchCompetitorGuard;
import com.github.phati.guardrails4j.interceptor.GuardRailsEngineInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableConfigurationProperties(
        CompetitorGuardConfigurationProperties.class
)
public class GuardRailsAutoConfiguration {

    private final CompetitorGuardConfigurationProperties competitorGuardConfigurationProperties;

    public GuardRailsAutoConfiguration(CompetitorGuardConfigurationProperties competitorGuardConfigurationProperties) {
        this.competitorGuardConfigurationProperties = competitorGuardConfigurationProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "guard.competitors.static-text-based.enabled", havingValue = "true")
    public StaticTextSearchCompetitorGuard staticTextSearchCompetitorGuard() {
        return new StaticTextSearchCompetitorGuard(competitorGuardConfigurationProperties.getStaticTextBased());
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "guardrails.enabled", havingValue = "true")
    public GuardRailsEngine guardRailsEngine(List<Guard> guards) {
        return new GuardRailsEngine(guards);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "guardrails.enabled", havingValue = "true")
    public GuardRailsEngineInterceptor guardRailsEngineInterceptor(GuardRailsEngine guardRailsEngine) {
        return new GuardRailsEngineInterceptor(guardRailsEngine);
    }

}
