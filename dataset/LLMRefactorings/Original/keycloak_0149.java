public class keycloak_0149 {

        @Override
        public boolean supports(TokenExchangeContext context) {
            // Subject impersonation request
            String requestedSubject = context.getFormParams().getFirst(OAuth2Constants.REQUESTED_SUBJECT);
            if (requestedSubject != null) {
                context.setUnsupportedReason("Parameter 'requested_subject' is not supported for standard token exchange");
                return false;
            }
    
            // Internal-external token exchange
            String requestedIssuer = context.getFormParams().getFirst(OAuth2Constants.REQUESTED_ISSUER);
            if (requestedIssuer != null) {
                context.setUnsupportedReason("Parameter 'requested_issuer' is not supported for standard token exchange");
                return false;
            }
    
            // External-internal token exchange
            String subjectIssuer = context.getFormParams().getFirst(OAuth2Constants.SUBJECT_ISSUER);
            if (subjectIssuer != null) {
                context.setUnsupportedReason("Parameter 'subject_issuer' is not supported for standard token exchange");
                return false;
            }
    
            if(!OIDCAdvancedConfigWrapper.fromClientModel(context.getClient()).isStandardTokenExchangeEnabled()) {
                context.setUnsupportedReason("Standard token exchange is not enabled for the requested client");
                return false;
            }
    
            String subjectToken = context.getParams().getSubjectToken();
            if (subjectToken == null) {
                context.setUnsupportedReason("Parameter 'subject_token' required for standard token exchange");
                return false;
            }
    
            String subjectTokenType = context.getParams().getSubjectTokenType();
            if (subjectTokenType == null) {
                context.setUnsupportedReason("Parameter 'subject_token_type' required for standard token exchange");
                return false;
            }
    
            if (!subjectTokenType.equals(OAuth2Constants.ACCESS_TOKEN_TYPE)) {
                context.setUnsupportedReason("Parameter 'subject_token' supports access tokens only");
                return false;
            }
    
            return true;
        }
}
