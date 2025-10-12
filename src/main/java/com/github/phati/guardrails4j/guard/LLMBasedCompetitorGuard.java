package com.github.phati.guardrails4j.guard;

import com.github.phati.guardrails4j.configuration.GuardConfigProperties;
import com.github.phati.guardrails4j.model.CompetitorGuardLLMResponse;
import com.github.phati.guardrails4j.model.GuardDecision;
import com.github.phati.guardrails4j.model.GuardResponse;
import com.github.phati.guardrails4j.model.UserQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.ai.chat.client.ChatClient;

@Log4j2
public class LLMBasedCompetitorGuard implements CompetitorGuard {

    public static final String SYSTEM_PROMPT = """
            You are a content safety assistant. Your job is to evaluate user queries and **block any query that asks for comparisons, rankings, or evaluations involving our company and its competitors**.
            
            The competitors to monitor are: {COMPETITOR_NAMES}
            Our Company name is: {OUR_COMPANY_NAME}
            
            Output format:
            - If blocked: `{ "action": "BLOCKED", "reason": "Competitor comparison detected" }`
            - If allowed: `{ "action": "ALLOWED" }`
            
            Guidelines:
            1. Block queries that attempt to find out if a competitor is better than us, or compare features, prices, or quality.
            2. Block queries even if phrased indirectly or with paraphrasing, as long as they reference one of the competitors listed above.
            3. If a query is clearly unrelated to competitor comparisons, allow it.
            4. Do not make subjective judgments; your only task is to **decide whether the query attempts to compare with a competitor**.
            """;
    private final Integer order;
    private final ChatClient chatClient;
    private final GuardConfigProperties.CompetitorGuard competitorGuard;

    public LLMBasedCompetitorGuard(ChatClient chatClient, GuardConfigProperties.CompetitorGuard competitorGuard) {
        this.order = competitorGuard.getLlmBased().getOrder();
        this.chatClient = chatClient;
        this.competitorGuard = competitorGuard;
    }

    @Override
    public GuardResponse evaluate(UserQuery userQuery) {
        log.debug("LLMBasedCompetitorGuard: Evaluating user query: {} with llm call", userQuery.getQuery());
        CompetitorGuardLLMResponse competitorGuardLLMResponse = chatClient.prompt()
                .user(userQuery.getQuery())
                .system(SYSTEM_PROMPT.replace("{COMPETITOR_NAMES}", String.join(", ", competitorGuard.getCompetitorList()))
                        .replace("{OUR_COMPANY_NAME}", competitorGuard.getOurCompanyName()))
                .call().entity(CompetitorGuardLLMResponse.class);

        log.debug("LLMBasedCompetitorGuard: LLM response: {}", competitorGuardLLMResponse);

        if (competitorGuardLLMResponse == null || competitorGuardLLMResponse.getAction() == null || competitorGuardLLMResponse.getAction().equalsIgnoreCase("BLOCKED")) {
            return GuardResponse.builder()
                    .decision(GuardDecision.BLOCK)
                    .message(competitorGuard.getDefaultResponse())
                    .guardName("LLMBasedCompetitorGuard")
                    .build();
        } else {
            return GuardResponse.builder()
                    .decision(GuardDecision.ALLOW)
                    .message("No competitor comparison detected")
                    .build();
        }
    }

    @Override
    public Integer getOrder() {
        return this.order;
    }

}
