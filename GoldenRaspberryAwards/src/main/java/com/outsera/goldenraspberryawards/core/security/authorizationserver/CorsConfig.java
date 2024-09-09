package com.outsera.goldenraspberryawards.core.security.authorizationserver;

import com.outsera.goldenraspberryawards.core.security.SecurityProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean(SecurityProperties securityProperties, Environment env) {
        CorsConfiguration config = new CorsConfiguration();

        List<String> allowedOriginsList;

        if (env.acceptsProfiles(Profiles.of("ignore_cors"))) {
            config.setAllowCredentials(false);
//            config.applyPermitDefaultValues();
            allowedOriginsList = Collections.singletonList("*");

        } else {
            config.setAllowCredentials(true); // isso é um ponto critico, verificar quando em producao para hosts especificos
            String[] allowedOrigins = securityProperties.getSecurity().getOriginAllowed().split(",");
            allowedOriginsList = Arrays.stream(allowedOrigins).toList();
        }
        List<String> allowedMethods = new ArrayList<String>() {{
            add("GET");
            add("PUT");
            add("DELETE");
            add("POST");
            add("PATCH");
            add("OPTIONS");
        }};
        List<String> allowedHeaders = new ArrayList<String>() {{
            add("Authorization");
            add("Content-Type");
            add("Accept");
        }};
        config.setAllowedOrigins(allowedOriginsList);
        config.setAllowedMethods(allowedMethods);
        config.setAllowedHeaders(allowedHeaders);
        config.setMaxAge(Duration.ofMinutes(60));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);

        return bean;
    }

}