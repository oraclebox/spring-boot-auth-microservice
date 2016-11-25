package me.oraclebox.auth.service.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@ConfigurationProperties(prefix = "application")
class ApplicationProperty {
    long jwtTTL;
    String jwtPhase;
    String jwtIssuer;
}
