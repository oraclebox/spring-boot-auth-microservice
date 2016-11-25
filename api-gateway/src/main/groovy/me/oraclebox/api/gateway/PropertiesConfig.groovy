package me.oraclebox.api.gateway

import me.oraclebox.api.gateway.property.ApplicationProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

/**
 *  Created by oraclebox on 12/4/2016.
 */
@Configuration
@EnableConfigurationProperties([ApplicationProperty.class])
class PropertiesConfig {
}
