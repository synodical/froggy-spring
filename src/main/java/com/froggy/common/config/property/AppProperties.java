package com.froggy.common.config.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.List;
@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "application")
public class AppProperties {
    private final Redis redis;
    private final Contract contract;

    @Getter
    public static class Redis {
        private final String host;
        private final Integer port;

        public Redis(String host, @DefaultValue("6379") Integer port) {
            this.host = host;
            this.port = port;
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class Contract {
        // User가 Shop을 만들기 위해 동의해야하는 필수 약관들
        private final List<String> requiredContractCodes;
    }
}
