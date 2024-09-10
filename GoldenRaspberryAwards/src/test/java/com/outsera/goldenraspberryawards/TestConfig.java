package com.outsera.goldenraspberryawards;

import com.outsera.goldenraspberryawards.core.database.DatabaseProperties;
import com.outsera.goldenraspberryawards.core.io.Base64ProtocolResolver;
import com.outsera.goldenraspberryawards.core.security.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@TestConfiguration
@EnableConfigurationProperties( {SecurityProperties.class, DatabaseProperties.class} )
public class TestConfig {
}
