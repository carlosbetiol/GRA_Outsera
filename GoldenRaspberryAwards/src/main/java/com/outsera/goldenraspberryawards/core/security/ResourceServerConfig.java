package com.outsera.goldenraspberryawards.core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.ForwardedHeaderFilter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class ResourceServerConfig {

	@Bean
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http, SecurityProperties securityProperties,
														  CustomAuthenticationProvider customAuthenticationProvider,
														  CustomAuthenticationFailureHandler customAuthenticationFailureHandler,
														  CustomLogoutSucessHandler customLogoutSucessHandler) throws Exception {

		http
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers("/login").permitAll()
						.requestMatchers("/actuator/**").permitAll()
						.requestMatchers("/version.json").permitAll()
						.requestMatchers("/localazy").permitAll()
						.requestMatchers("/assets/**").permitAll()
						.anyRequest().authenticated())
				.csrf(AbstractHttpConfigurer::disable)
				.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

		http.logout(
				logout -> {
					logout.logoutSuccessHandler(customLogoutSucessHandler);
				}
		);

		http.authenticationProvider(customAuthenticationProvider);

		return http.formLogin(customizer -> customizer.loginPage("/login")
				.failureHandler(customAuthenticationFailureHandler)).build();
	}

//    @Bean
//    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
//        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
//    }

	public JwtDecoder jwtDecoder(SecurityProperties securityProperties) {
		return JwtDecoders.fromIssuerLocation(securityProperties.getSecurity().getAuthServerUrl());
	}

    @Bean
    public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
    }
    
    private JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
			List<String> authorities = jwt.getClaimAsStringList("authorities");

			if (authorities == null) {
				authorities = Collections.emptyList();
			}

			JwtGrantedAuthoritiesConverter scopesAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
			Collection<GrantedAuthority> grantedAuthorities = scopesAuthoritiesConverter.convert(jwt);

			grantedAuthorities.addAll(authorities.stream()
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList()));

			return grantedAuthorities;

		});

		return jwtAuthenticationConverter;

	}

	// to maintain the requested protocol (http or https)
	@Bean
	ForwardedHeaderFilter forwardedHeaderFilter() {
		return new ForwardedHeaderFilter();
	}

}
