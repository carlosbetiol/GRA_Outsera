package com.outsera.goldenraspberryawards.core.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Objects;

@Validated
@ConfigurationProperties("awardsapi")
public class SecurityProperties {

    private final Security security = new Security();

    public Security getSecurity() {
        return security;
    }

    @Validated
    public static class Security {

        @NotEmpty
        private List<String> allowedRedirects;
        @NotBlank
        private String authServerUrl;
        @NotBlank
        private String adminEmailAddress;
        @NotBlank
        private String adminInitPassword;
        @NotBlank
        private String originAllowed = "http://xxxx.com:8080";

        private ClientCredentials clientCredentials = new ClientCredentials();

        public List<String> getAllowedRedirects() {
            return allowedRedirects;
        }

        public void setAllowedRedirects(List<String> allowedRedirects) {
            this.allowedRedirects = allowedRedirects;
        }

        public String getAuthServerUrl() {
            return authServerUrl;
        }

        public void setAuthServerUrl(String authServerUrl) {
            this.authServerUrl = authServerUrl;
        }

        public String getAdminInitPassword() {
            return adminInitPassword;
        }

        public void setAdminInitPassword(String adminInitPassword) {
            this.adminInitPassword = adminInitPassword;
        }

        public String getAdminEmailAddress() {
            return adminEmailAddress;
        }

        public void setAdminEmailAddress(String adminEmailAddress) {
            this.adminEmailAddress = adminEmailAddress;
        }

        public ClientCredentials getClientCredentials() {
            return clientCredentials;
        }

        public String getOriginAllowed() {
            return originAllowed;
        }

        public void setOriginAllowed(String originAllowed) {
            this.originAllowed = originAllowed;
        }

        public void setClientCredentials(ClientCredentials clientCredentials) {
            this.clientCredentials = clientCredentials;
        }

        @Validated
        public class ClientCredentials {

            private ClientCredential frontweb = new ClientCredential();
            private ClientCredential integration = new ClientCredential();

            public ClientCredential getFrontweb() {
                return frontweb;
            }
            public ClientCredential getIntegration() {
                return integration;
            }

            public void setIntegration(ClientCredential integration) {
                this.integration = integration;
            }

            public void setFrontweb(ClientCredential frontweb) {
                this.frontweb = frontweb;
            }

            @Validated
            public class ClientCredential {
                @NotBlank
                private String clientId;
                @NotBlank
                private String clientPassword;

                public String getClientId() {
                    return clientId;
                }

                public void setClientId(String clientId) {
                    this.clientId = clientId;
                }

                public String getClientPassword() {
                    return clientPassword;
                }

                public void setClientPassword(String clientPassword) {
                    this.clientPassword = clientPassword;
                }

                @Override
                public boolean equals(Object o) {
                    if (this == o) return true;
                    if (o == null || getClass() != o.getClass()) return false;
                    ClientCredential that = (ClientCredential) o;
                    return clientId.equals(that.clientId) && clientPassword.equals(that.clientPassword);
                }

                @Override
                public int hashCode() {
                    return Objects.hash(clientId, clientPassword);
                }

                @Override
                public String toString() {
                    return "ClientCredential{" +
                            "clientId='" + clientId + '\'' +
                            '}';
                }
            }
        }
    }
}
