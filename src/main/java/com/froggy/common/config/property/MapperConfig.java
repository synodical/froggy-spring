package com.froggy.common.config.property;

import org.mapstruct.ReportingPolicy;

@org.mapstruct.MapperConfig(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public class MapperConfig {
}

