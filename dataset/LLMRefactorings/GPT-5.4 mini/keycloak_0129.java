public class keycloak_0129 {

        private static final String USER_INFO_CALL_FAILURE = "user info call failure";

        protected BrokeredIdentityContext validateExternalTokenThroughUserInfo(EventBuilder event, String subjectToken, String subjectTokenType) {
            event.detail("validation_method", "user info");

            SimpleHttpResponse response = null;
            int status = 0;
            try {
                String userInfoUrl = getProfileEndpointForValidation(event);
                response = buildUserInfoRequest(subjectToken, userInfoUrl).asResponse();
                status = response.getStatus();
            } catch (IOException e) {
                logger.debug("Failed to invoke user info for external exchange", e);
            }
            if (status != 200) {
                logger.debugf("Failed to invoke user info status: %d", status);
                event.detail(Details.REASON, USER_INFO_CALL_FAILURE);
                event.error(Errors.INVALID_TOKEN);
                throw new ErrorResponseException(OAuthErrorException.INVALID_TOKEN, "invalid token", Response.Status.BAD_REQUEST);
            }
            JsonNode profile = null;
            try {
                profile = response.asJson();
            } catch (IOException e) {
                event.detail(Details.REASON, USER_INFO_CALL_FAILURE);
                event.error(Errors.INVALID_TOKEN);
                throw new ErrorResponseException(OAuthErrorException.INVALID_TOKEN, "invalid token", Response.Status.BAD_REQUEST);
            }
            BrokeredIdentityContext context = extractIdentityFromProfile(event, profile);
            if (context.getId() == null) {
                event.detail(Details.REASON, USER_INFO_CALL_FAILURE);
                event.error(Errors.INVALID_TOKEN);
                throw new ErrorResponseException(OAuthErrorException.INVALID_TOKEN, "invalid token", Response.Status.BAD_REQUEST);
            }
            return context;
        }
}
