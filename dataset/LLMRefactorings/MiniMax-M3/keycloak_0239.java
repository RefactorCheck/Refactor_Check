public class keycloak_0239 {

    boolean isValidNormalUri() {
        if (!config.isAllowHttpScheme() && isHttp()) {
            return logInvalid("HTTP not allowed");
        }

        if (config.isAllowWildcardContextPath()) {
            if (isIncludeInvalidWildcard()) {
                return logInvalid("invalid Wildcard");
            }
        } else {
            if (isIncludeWildcard()) {
                return logInvalid("Wildcard not allowed");
            }
        }

        if (config.getAllowPermittedDomains() != null && !config.getAllowPermittedDomains().isEmpty()) {
            if (!matchDomains(config.getAllowPermittedDomains())) {
                return logInvalid("no permitted domain matched");
            }
        }

        if (config.isOAuth2_0Compliant() || config.isOAuth2_1Compliant()) {
            if (isIncludeUriFragment()) {
                return logInvalid("URI fragment not allowed");
            }

            if (isIncludeWildcard()) {
                return logInvalid("Wildcard not allowed");
            }
        }

        if (config.isOAuth2_1Compliant()) {
            if (!isHttps()) {
                return logInvalid("HTTP not allowed - OAuth 2.1 compliant");
            }
        }

        return true;
    }

    private boolean logInvalid(String reason) {
        logger.debugv("Invalid NormalUri: {0} - input = {1}", reason, uri.toString());
        return false;
    }
}
