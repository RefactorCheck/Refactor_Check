public class keycloak_0149 {

        @Override
        public boolean supports(TokenExchangeContext context) {
            // Subject impersonation request
            if (hasUnsupportedFormParam(context, OAuth2Constants.REQUESTED_SUBJECT, "requested_subject")) {
                return false;
            }
    
            // Internal-external token exchange
            if (hasUnsupportedFormParam(context, OAuth2Constants.REQUESTED_ISSUER, "requested_issuer")) {
                return false;
            }
    
            // External-internal token exchange
            if (hasUnsupportedFormParam(context, OAuth2Constants.SUBJECT_ISSUER, "subject_issuer")) {
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

        private boolean hasUnsupportedFormParam(TokenExchangeContext context, String paramName, String displayName) {
            if (context.getFormParams().getFirst(paramName) != null) {
                context.setUnsupportedReason("Parameter '" + displayName + "' is not supported for standard token exchange");
                return true;
            }
            return false;
        }
}
