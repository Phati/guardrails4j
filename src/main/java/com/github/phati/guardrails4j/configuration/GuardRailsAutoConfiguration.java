package com.github.phati.guardrails4j.configuration;

import com.github.phati.guardrails4j.engine.GuardRailsEngine;
import com.github.phati.guardrails4j.guard.Guard;
import com.github.phati.guardrails4j.guard.LLMBasedCompetitorGuard;
import com.github.phati.guardrails4j.guard.SimpleTextSearchCompetitorGuard;
import com.github.phati.guardrails4j.guard.VectorBasedCompetitorGuard;
import com.github.phati.guardrails4j.interceptor.GuardRailsEngineInterceptor;
import lombok.extern.log4j.Log4j2;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Log4j2
@Configuration
@EnableConfigurationProperties(
        GuardConfigProperties.class
)
public class GuardRailsAutoConfiguration {

    private final GuardConfigProperties guardConfigProperties;

    public GuardRailsAutoConfiguration(GuardConfigProperties guardConfigProperties) {
        this.guardConfigProperties = guardConfigProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "guard.competitors.simple-text-based.enabled", havingValue = "true")
    public SimpleTextSearchCompetitorGuard simpleTextSearchCompetitorGuard() {
        return new SimpleTextSearchCompetitorGuard(guardConfigProperties.getCompetitors());
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "guard.competitors.llm-based.enabled", havingValue = "true")
    public LLMBasedCompetitorGuard llmBasedCompetitorGuard(ApplicationContext applicationContext) {
        log.info("Initializing LLM Based Competitor Guard with ChatClient Bean Name: {}", guardConfigProperties.getCompetitors().getLlmBased().getChatClientBeanName());
        ChatClient chatClient = applicationContext.getBean(guardConfigProperties.getCompetitors().getLlmBased().getChatClientBeanName(), ChatClient.class);
        return new LLMBasedCompetitorGuard(chatClient, guardConfigProperties.getCompetitors());
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "guard.competitors.vector-based.enabled", havingValue = "true")
    public VectorBasedCompetitorGuard vectorBasedCompetitorGuard(ApplicationContext applicationContext) {
        log.info("Initializing Vector Based Competitor Guard with VectorStore Bean Name: {}", guardConfigProperties.getCompetitors().getVectorBased().getVectorStoreBeanName());
        VectorStore vectorStore = applicationContext.getBean(guardConfigProperties.getCompetitors().getVectorBased().getVectorStoreBeanName(), VectorStore.class);
        return new VectorBasedCompetitorGuard(vectorStore, guardConfigProperties.getCompetitors());
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
