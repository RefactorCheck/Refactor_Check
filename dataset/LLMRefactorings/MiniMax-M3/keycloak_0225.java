public class keycloak_0225 {

    @Override
    public void init(Config.Scope config) {
        this.providerConfig = new OIDCProviderConfig(config);
        logConfigurationWarnings();
        initBuiltIns();
    }

    private void logConfigurationWarnings() {
        if (this.providerConfig.isAllowMultipleAudiencesForJwtClientAuthentication()) {
            logger.warnf("It is allowed to have multiple audiences for the JWT client authentication. This option is not recommended and will be removed in one of the future releases."
                    + " It is recommended to update your OAuth/OIDC clients to rather use single audience in the JWT token used for the client authentication.");
        }

        if (this.providerConfig.isAllowTokenIntrospectionWithoutAudienceCheck()) {
            logger.warnf("Token introspection audience check is disabled globally. " +
                    "Any authenticated client can introspect any token. " +
                    "Enable per-client settings and disable this option: %s=false",
                    CONFIG_ALLOW_TOKEN_INTROSPECTION_WITHOUT_AUDIENCE_CHECK);
        }

        if (this.providerConfig.isAllowUserinfoWithLightweightAccessToken()) {
            logger.warnf("UserInfo endpoint accepts lightweight access tokens globally. " +
                    "Lightweight tokens should use token introspection instead. " +
                    "Enable per-client settings and disable this option: %s=false",
                    CONFIG_ALLOW_USERINFO_WITH_LIGHTWEIGHT_ACCESS_TOKEN);
        }
    }
}
