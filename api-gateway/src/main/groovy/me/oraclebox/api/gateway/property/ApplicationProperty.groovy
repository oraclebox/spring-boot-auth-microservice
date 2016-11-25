package me.oraclebox.api.gateway.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "application")
class ApplicationProperty {
    String authServiceUrl;
}
