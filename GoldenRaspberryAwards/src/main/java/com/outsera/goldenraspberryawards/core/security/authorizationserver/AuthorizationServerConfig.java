/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.outsera.goldenraspberryawards.core.security.authorizationserver;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.outsera.goldenraspberryawards.api.contextual.ContextualRequest;
import com.outsera.goldenraspberryawards.core.security.CustomAuthenticationFailureHandler;
import com.outsera.goldenraspberryawards.core.security.CustomLogoutSucessHandler;
import com.outsera.goldenraspberryawards.core.security.SecurityProperties;
import com.outsera.goldenraspberryawards.domain.model.RequestLog;
import com.outsera.goldenraspberryawards.domain.model.User;
import com.outsera.goldenraspberryawards.domain.repository.PermissionRepository;
import com.outsera.goldenraspberryawards.domain.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfig {

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,
																	  CustomAuthenticationFailureHandler customAuthenticationFailureHandler,
																	  CustomLogoutSucessHandler customLogoutSucessHandler) throws Exception {
//		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
		OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
				new OAuth2AuthorizationServerConfigurer();

		RequestMatcher endpointsMatcher = authorizationServerConfigurer
				.getEndpointsMatcher();

		http.securityMatcher(endpointsMatcher)
				.authorizeHttpRequests(authorize -> { authorize
						.requestMatchers("/h2-console/**").permitAll()
						.requestMatchers("/login").permitAll()
						.requestMatchers("/actuator/**").permitAll()
						.requestMatchers("/version.json").permitAll()
						.requestMatchers("/localazy").permitAll()
						.requestMatchers("/assets/**").permitAll()
						.anyRequest().authenticated();
				})
				.csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
				.formLogin(Customizer.withDefaults())
				.exceptionHandling(exceptions -> {
					exceptions.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"));
				})
//				.apply(authorizationServerConfigurer);
				.with(authorizationServerConfigurer, Customizer.withDefaults());

		http.logout(
				logout -> {
					logout
							.logoutSuccessHandler(customLogoutSucessHandler);
				}
		);

//		return http.build();

		return http.formLogin(customizer -> customizer.loginPage("/login")
				.failureHandler(customAuthenticationFailureHandler)).build();

//		http
//				.authorizeHttpRequests(authorize -> authorize
//						.requestMatchers("/**").permitAll()
//						.requestMatchers("/login").permitAll()
//						.requestMatchers("/actuator/**").permitAll()
//						.requestMatchers("/version.json").permitAll()
//						.requestMatchers("/localazy").permitAll()
//						.requestMatchers("/assets/**").permitAll()
//						.anyRequest().authenticated())
//				.csrf(AbstractHttpConfigurer::disable)
//				.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

//		OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
//				new OAuth2AuthorizationServerConfigurer();
//
//		authorizationServerConfigurer.authorizationEndpoint(
//				customizer -> customizer.consentPage("/oauth2/consent"));
//
//		RequestMatcher endpointsMatcher = authorizationServerConfigurer
//				.getEndpointsMatcher();
//
//		http.securityMatcher(endpointsMatcher)
//				.authorizeHttpRequests(authorize -> {
//					authorize.anyRequest().authenticated();
//				})
//				.csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
//				.formLogin(Customizer.withDefaults())
//				.exceptionHandling(exceptions -> {
//					exceptions.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"));
//				})
//				.apply(authorizationServerConfigurer);
//
//		return http.formLogin(customizer -> customizer.loginPage("/login")).build();

	}

	@Bean
	public RegisteredClientRepository registeredClientRepository(JdbcOperations jdbcOperations) {
		return new JdbcRegisteredClientRepository(jdbcOperations);
	}

	@Bean
	public OAuth2AuthorizationService authorizationService(JdbcOperations jdbcOperations, RegisteredClientRepository registeredClientRepository) {
		return new JdbcOAuth2AuthorizationService( jdbcOperations, registeredClientRepository );
	}

	@Bean
	public JWKSource<SecurityContext> jwkSource(JwtKeyStoreProperties jwtKeyStoreProperties) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, JOSEException {

		final InputStream inputStream = jwtKeyStoreProperties.getJksLocation().getInputStream();
		final KeyStore keyStore = KeyStore.getInstance("JKS");
		keyStore.load(inputStream, jwtKeyStoreProperties.getPassword().toCharArray());

		RSAKey rsaKey = RSAKey.load(
				keyStore,
				jwtKeyStoreProperties.getKeypairAlias(),
				jwtKeyStoreProperties.getPassword().toCharArray()
		);

		JWKSet jwkSet = new JWKSet(rsaKey);
		return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
	}

	@Bean
	public AuthorizationServerSettings providerSettings(SecurityProperties securityProperties) {

		return AuthorizationServerSettings.builder()
				.issuer(securityProperties.getSecurity().getAuthServerUrl())
				.build();

	}

	@Bean
	public OAuth2TokenCustomizer<JwtEncodingContext> jwtBuildCustomizer(
			UserService userService,
			SecurityProperties securityProperties,
			PermissionRepository permissionRepository,
			ContextualRequest contextualRequest) {
		return (context) -> {

			if(context.getPrincipal() instanceof UsernamePasswordAuthenticationToken) {

				UsernamePasswordAuthenticationToken authenticationToken = context.getPrincipal();

				Object obj = authenticationToken.getPrincipal();

				org.springframework.security.core.userdetails.User userPrincipal = (org.springframework.security.core.userdetails.User) authenticationToken.getPrincipal();

				Set<String> authorities = new HashSet<>();
				for (GrantedAuthority grantedAuthority : userPrincipal.getAuthorities()) {
					authorities.add(grantedAuthority.getAuthority());
				}

				User user = userService.findByEmailToAuthentication(userPrincipal.getUsername());
				context.getClaims().claim("userId", user.getId().toString());
				context.getClaims().claim("name", user.getName());
				context.getClaims().claim("email", user.getEmail());
				context.getClaims().claim("isAdmin", user.isAdmin() ? "yes" : "no");

				context.getClaims().claim("authorities", authorities);

				if (contextualRequest.getRequestLog().isPresent()) {

					RequestLog requestLog = contextualRequest.getRequestLog().get();

					Map<String, String> headersMap = new HashMap<>();
					requestLog.setResponseStatus(HttpStatus.OK.value());
					requestLog.setResponseTime(OffsetDateTime.now());

					contextualRequest.triggerRegisterRequestLog(user.getId());

				}

			} else if (context.getPrincipal() instanceof OAuth2ClientAuthenticationToken){
				// quando é client credentials para colocar os authorities
//				OAuth2ClientAuthenticationToken authenticationToken = context.getPrincipal();
//				if (authenticationToken.getRegisteredClient().getClientId()
//						.equals(securityProperties.getSecurity().getClientCredentials().getFrontweb().getClientId())) {
//					String walletAddress = ResourceUriHelper.getWalletAddress();
//					ExternalAuth externalAuth = externalAuthRepository.findByWalletAddress(walletAddress)
//							.orElseThrow(() -> new NonceNotFoundException(walletAddress));
//					Set<String> authorities = new HashSet<>();
//					externalAuth.getGroups().forEach(g -> g.getPermissions()
//							.forEach(p -> authorities.add(p.getIdentifier().toUpperCase())));
//					context.getClaims().claim("userId", externalAuth.getId());
//					context.getClaims().claim("userName", walletAddress);
//					context.getClaims().claim("authorities", authorities);
//				} else if (authenticationToken.getRegisteredClient().getClientId()
//						.equals(securityProperties.getSecurity().getClientCredentials().getGame().getClientId())) {
//					Set<String> authorities = new HashSet<>();
//					List<Permission> permissions = permissionRepository.findAllByTypes(PermissionType.AUTHENTICATOR);
//					permissions.forEach(permission -> authorities.add(permission.getIdentifier()));
//					context.getClaims().claim("authorities", authorities);
//				}

			} else {
				throw new AccessDeniedException("Invalid grant type to customize token");
			}
		};
	}

}
