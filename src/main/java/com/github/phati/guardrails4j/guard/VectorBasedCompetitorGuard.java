package com.github.phati.guardrails4j.guard;

import com.github.phati.guardrails4j.configuration.GuardConfigProperties;
import com.github.phati.guardrails4j.model.GuardDecision;
import com.github.phati.guardrails4j.model.GuardResponse;
import com.github.phati.guardrails4j.model.UserQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;

import java.util.List;

@Log4j2
public class VectorBasedCompetitorGuard implements CompetitorGuard {

    private final VectorStore vectorStore;
    private final Integer order;
    private final GuardConfigProperties.CompetitorGuard competitorGuard;

    public VectorBasedCompetitorGuard(VectorStore vectorStore, GuardConfigProperties.CompetitorGuard competitorGuard) {
        this.vectorStore = vectorStore;
        this.order = competitorGuard.getVectorBased().getOrder();
        this.competitorGuard = competitorGuard;
    }

    @Override
    public GuardResponse evaluate(UserQuery userQuery) {
        log.debug("VectorBasedCompetitorGuard: Evaluating user query: {}", userQuery.getQuery());
        List<Document> documents = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(userQuery.getQuery())
                        .topK(competitorGuard.getVectorBased().getTopK())
                        .similarityThreshold(competitorGuard.getVectorBased().getSimilarityThreshold())
                        .build()
        );

        log.debug("VectorBasedCompetitorGuard: Found {} similar documents", documents.size());
        documents.forEach(document ->{
            log.debug("Document: {} | Score: {}", document.getText(), document.getMetadata().get("score"));
        });

        if (!documents.isEmpty()) {
            return GuardResponse.builder()
                    .decision(GuardDecision.BLOCK)
                    .message(competitorGuard.getDefaultResponse())
                    .guardName("VectorBasedCompetitorGuard")
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
