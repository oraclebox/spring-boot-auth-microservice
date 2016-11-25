package me.oraclebox.auth.service.config

import me.oraclebox.auth.service.property.ApplicationProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

/**
 *  Created by oraclebox on 12/4/2016.
 */
@Configuration
@EnableConfigurationProperties([ApplicationProperty.class])
class PropertiesConfig {
}
