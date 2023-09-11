package com.froggy.common.config.property;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationPropertiesScan(basePackages = {"com.froggy.common.config.property"})
public class PropertyConfig {
}
