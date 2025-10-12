package com.github.phati.guardrails4j.guard;

import com.github.phati.guardrails4j.model.GuardResponse;
import com.github.phati.guardrails4j.model.UserQuery;

public interface Guard extends Comparable<Guard> {

    GuardResponse evaluate(UserQuery userQuery);

    Integer getOrder();

    @Override
    default int compareTo(Guard o) {
        return this.getOrder().compareTo(o.getOrder());
    }

}
