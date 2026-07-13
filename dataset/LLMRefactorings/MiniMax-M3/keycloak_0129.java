public class keycloak_0129 {

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
                failWithInvalidToken(event);
            }
            JsonNode profile = null;
            try {
                profile = response.asJson();
            } catch (IOException e) {
                failWithInvalidToken(event);
            }
            BrokeredIdentityContext context = extractIdentityFromProfile(event, profile);
            if (context.getId() == null) {
                failWithInvalidToken(event);
            }
            return context;
        }

        private void failWithInvalidToken(EventBuilder event) {
            event.detail(Details.REASON, "user info call failure");
            event.error(Errors.INVALID_TOKEN);
            throw new ErrorResponseException(OAuthErrorException.INVALID_TOKEN, "invalid token", Response.Status.BAD_REQUEST);
        }
}
