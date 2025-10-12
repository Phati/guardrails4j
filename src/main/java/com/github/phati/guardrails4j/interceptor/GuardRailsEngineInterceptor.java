package com.github.phati.guardrails4j.interceptor;

import com.github.phati.guardrails4j.engine.GuardRailsEngine;
import com.github.phati.guardrails4j.exception.GuardException;
import com.github.phati.guardrails4j.model.GuardRailsEngineResponse;
import com.github.phati.guardrails4j.model.UserQuery;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class GuardRailsEngineInterceptor implements HandlerInterceptor {

    private final GuardRailsEngine guardRailsEngine;

    public GuardRailsEngineInterceptor(GuardRailsEngine guardRailsEngine) {
        this.guardRailsEngine = guardRailsEngine;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserQuery query = extractUserQuery(request);
        GuardRailsEngineResponse guardRailsEngineResponse = guardRailsEngine.evaluateAll(query);
        if (!guardRailsEngineResponse.isAllow()) {
            throw new GuardException(guardRailsEngineResponse.getFaildGuardResponse().getMessage());
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
