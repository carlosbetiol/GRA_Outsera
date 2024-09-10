package com.outsera.goldenraspberryawards.core.security.authorizationserver;

import com.outsera.goldenraspberryawards.core.security.SecurityProperties;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

//@Component
@Log4j2
@Profile({"dev", "prod"})
public class Oauth2ClientInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final SecurityProperties securityProperties;

    private final RegisteredClientRepository registeredClientRepository;

    private final Environment environment;

    public Oauth2ClientInitializer(SecurityProperties securityProperties,
                                   RegisteredClientRepository registeredClientRepository,
                                   Environment environment) {
        this.securityProperties = securityProperties;
        this.registeredClientRepository = registeredClientRepository;
        this.environment = environment;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        log.info("Initializing Oauth2 Clients");
        createOrUpdateFrontWebOauth2Client();
        createOrUpdateIntegrationbOauth2Client();

    }

    private void createOrUpdateFrontWebOauth2Client() {

        String clientId = Optional.ofNullable(registeredClientRepository.findByClientId(securityProperties.getSecurity().getClientCredentials().getFrontweb().getClientId()))
                .map(RegisteredClient::getId)
                .orElse( UUID.randomUUID().toString() );

        RegisteredClient registeredClient;
        if (environment.acceptsProfiles(Profiles.of("dev")))
            registeredClient = RegisteredClient.withId(clientId)
                .clientId(securityProperties.getSecurity().getClientCredentials().getFrontweb().getClientId())
                .clientSecret(securityProperties.getSecurity().getClientCredentials().getFrontweb().getClientPassword())
                .clientName(securityProperties.getSecurity().getClientCredentials().getFrontweb().getClientId())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUris(uris -> uris.addAll(securityProperties.getSecurity().getAllowedRedirects()))
                .scope("READ")
                .scope("WRITE")
                .tokenSettings(TokenSettings.builder()
                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                        .accessTokenTimeToLive(Duration.ofDays(7))
                        .refreshTokenTimeToLive(Duration.ofDays(365))
                        .reuseRefreshTokens(false)
                        .build())
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(false)
                        .requireProofKey(false)
                        .build())
                    .clientIdIssuedAt(OffsetDateTime.now().toInstant())
                    .clientSecretExpiresAt(OffsetDateTime.now().plusYears(1).toInstant())
                    .postLogoutRedirectUri(securityProperties.getSecurity().getAuthServerUrl())
                    .build();
        else
            registeredClient = RegisteredClient.withId(clientId)
                    .clientId(securityProperties.getSecurity().getClientCredentials().getFrontweb().getClientId())
                    .clientSecret(securityProperties.getSecurity().getClientCredentials().getFrontweb().getClientPassword())
                    .clientName(securityProperties.getSecurity().getClientCredentials().getFrontweb().getClientId())
                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                    .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                    .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                    .redirectUris(uris -> uris.addAll(securityProperties.getSecurity().getAllowedRedirects()))
                    .scope("READ")
                    .scope("WRITE")
                    .tokenSettings(TokenSettings.builder()
                            .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                            .accessTokenTimeToLive(Duration.ofMinutes(60))
                            .refreshTokenTimeToLive(Duration.ofHours(3))
                            .reuseRefreshTokens(false)
                            .build())
                    .clientSettings(ClientSettings.builder()
                            .requireAuthorizationConsent(false)
                            .requireProofKey(false)
                            .build())
                    .clientIdIssuedAt(OffsetDateTime.now().toInstant())
                    .clientSecretExpiresAt(OffsetDateTime.now().plusYears(1).toInstant())
                    .postLogoutRedirectUri(securityProperties.getSecurity().getAuthServerUrl())
                    .build();

        registeredClientRepository.save(registeredClient);

    }

    private void createOrUpdateIntegrationbOauth2Client() {

        String clientId = Optional.ofNullable(registeredClientRepository.findByClientId(securityProperties.getSecurity().getClientCredentials().getIntegration().getClientId()))
                .map(RegisteredClient::getId)
                .orElse( UUID.randomUUID().toString() );

        RegisteredClient registeredClient = RegisteredClient.withId(clientId)
                .clientId(securityProperties.getSecurity().getClientCredentials().getIntegration().getClientId())
                .clientSecret(securityProperties.getSecurity().getClientCredentials().getIntegration().getClientPassword())
                .clientName(securityProperties.getSecurity().getClientCredentials().getIntegration().getClientId())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .scope("INTEGRATION")
                .tokenSettings(TokenSettings.builder()
                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                        .accessTokenTimeToLive(Duration.ofMinutes(60))
                        .build())
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(false)
                        .requireProofKey(false)
                        .build())
                .clientIdIssuedAt(OffsetDateTime.now().toInstant())
                .clientSecretExpiresAt(OffsetDateTime.now().plusYears(1).toInstant())
                .postLogoutRedirectUri(securityProperties.getSecurity().getAuthServerUrl())
                .build();

        registeredClientRepository.save(registeredClient);

    }


}
