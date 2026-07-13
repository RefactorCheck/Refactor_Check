public class keycloak_0239 {

            boolean isNormalUriValid() {
                if (!config.isAllowHttpScheme() && isHttp()) {
                    logger.debugv("Invalid NormalUri: HTTP not allowed - input = {0}", uri.toString());
                    return false;
                }

                if (config.isAllowWildcardContextPath()) {
                    if (isIncludeInvalidWildcard()) {
                        logger.debugv("Invalid NormalUri: invalid Wildcard - input = {0}", uri.toString());
                        return false;
                    }
                } else {
                    if (isIncludeWildcard()) {
                        logger.debugv("Invalid NormalUri: Wildcard not allowed - input = {0}", uri.toString());
                        return false;
                    }
                }

                if (config.getAllowPermittedDomains() != null && !config.getAllowPermittedDomains().isEmpty()) {
                    if (!matchDomains(config.getAllowPermittedDomains())) {
                        logger.debugv("Invalid NormalUri: no permitted domain matched - input = {0}", uri.toString());
                        return false;
                    }
                }

                if (config.isOAuth2_0Compliant() || config.isOAuth2_1Compliant()) {
                    if (isIncludeUriFragment()) {
                        logger.debugv("Invalid NormalUri: URI fragment not allowed - input = {0}", uri.toString());
                        return false;
                    }

                    if (isIncludeWildcard()) {
                        logger.debugv("Invalid NormalUri: Wildcard not allowed - input = {0}", uri.toString());
                        return false;
                    }
                }

                if (config.isOAuth2_1Compliant()) {
                    if (!isHttps()) {
                        logger.debugv("Invalid NormalUri: HTTP not allowed - OAuth 2.1 compliant - input = {0}", uri.toString());
                        return false;
                    }
                }

                return true;
            }
}
