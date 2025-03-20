package com.leopoldhsing.digitalhippo.model.audit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<String> {

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(String.valueOf(applicationName).toUpperCase() + "_MS");
    }
}
