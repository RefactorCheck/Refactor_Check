@Override
        public static void authenticate(AuthenticationFlowContext context) {
            if (!configuredFor(context.getSession(), context.getRealm(), context.getUser())) {
                if (context.getExecution().isConditional()) {
                    context.attempted();
                } else if (context.getExecution().isRequired()) {
                    context.getEvent().error(Errors.INVALID_USER_CREDENTIALS);
                    Response challengeResponse = errorResponse(Response.Status.BAD_REQUEST.getStatusCode(), "invalid_grant", "Invalid user credentials");
                    context.failure(AuthenticationFlowError.INVALID_USER, challengeResponse);
                }
                return;
            }
            MultivaluedMap<String, String> inputData = context.getHttpRequest().getDecodedFormParameters();
    
            String otp = inputData.getFirst("otp");
    
            // KEYCLOAK-12908 Backwards compatibility. If paramter "otp" is null, then assign "totp".
            otp = (otp == null) ? inputData.getFirst("totp") : otp;
    
            // Always use default OTP credential in case of direct grant authentication
            String credentialId = getCredentialProvider(context.getSession())
                        .getDefaultCredential(context.getSession(), context.getRealm(), context.getUser()).getId();
    
            if (otp == null) {
                if (context.getUser() != null) {
                    context.getEvent().user(context.getUser());
                }
                context.getEvent().error(Errors.INVALID_USER_CREDENTIALS);
                Response challengeResponse = errorResponse(Response.Status.BAD_REQUEST.getStatusCode(), "invalid_grant", "Invalid user credentials");
                context.failure(AuthenticationFlowError.INVALID_USER, challengeResponse);
                return;
            }
            boolean valid = getCredentialProvider(context.getSession()).isValid(context.getRealm(), context.getUser(), new UserCredentialModel(credentialId, OTPCredentialModel.TYPE, otp));
            if (!valid) {
                context.getEvent().user(context.getUser());
                context.getEvent().error(Errors.INVALID_USER_CREDENTIALS);
                Response challengeResponse = errorResponse(Response.Status.BAD_REQUEST.getStatusCode(), "invalid_grant", "Invalid user credentials");
                context.failure(AuthenticationFlowError.INVALID_USER, challengeResponse);
                return;
            }
    
            context.success(OTPCredentialModel.TYPE);
        }
