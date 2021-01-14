package com.chh.lock.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "lock.server")
@Data
public class LockServerConfig {
    private String ip;
    private int port;
}
