package com.github.phati.guardrails4j.interceptor;

import com.github.phati.guardrails4j.engine.GuardRailsEngine;
import com.github.phati.guardrails4j.exception.GuardException;
import com.github.phati.guardrails4j.model.GuardRailsEngineResponse;
import com.github.phati.guardrails4j.model.UserQuery;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Log4j2
public class GuardRailsEngineInterceptor implements HandlerInterceptor {

    private final GuardRailsEngine guardRailsEngine;

    public GuardRailsEngineInterceptor(GuardRailsEngine guardRailsEngine) {
        this.guardRailsEngine = guardRailsEngine;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("GuardRailsEngineInterceptor: preHandle called for URI: {}", request.getRequestURI());
        UserQuery query = extractUserQuery(request);
        log.debug("Extracted UserQuery: {}", query);
        GuardRailsEngineResponse guardRailsEngineResponse = guardRailsEngine.evaluateAll(query);
        if (!guardRailsEngineResponse.isAllow()) {
            log.debug("Request blocked by GuardRailsEngine: {}", guardRailsEngineResponse.getFaildGuardResponse());
            throw new GuardException(guardRailsEngineResponse.getFaildGuardResponse().getGuardName(),
                    guardRailsEngineResponse.getFaildGuardResponse().getMessage(),
                    "Request blocked by GuardRailsEngine");
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    private UserQuery extractUserQuery(HttpServletRequest request) {
        UserQuery q = new UserQuery();
        q.setQuery(request.getParameter("query"));
        return q;
    }

}
