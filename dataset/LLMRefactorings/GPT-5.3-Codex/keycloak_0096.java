public void parseRequest(BackchannelAuthenticationEndpointRequest request, boolean enableFeature) {
            request.scope = replaceIfNotNull(request.scope, getParameter(OIDCLoginProtocol.SCOPE_PARAM));
    
            request.clientNotificationToken = replaceIfNotNull(request.clientNotificationToken, getParameter(CibaGrantType.CLIENT_NOTIFICATION_TOKEN));
            request.acr = replaceIfNotNull(request.acr, getParameter(OIDCLoginProtocol.ACR_PARAM));
            request.loginHintToken = replaceIfNotNull(request.loginHintToken, getParameter(CibaGrantType.LOGIN_HINT_TOKEN));
            request.idTokenHint = replaceIfNotNull(request.idTokenHint, getParameter(OIDCLoginProtocol.ID_TOKEN_HINT));
            request.loginHint = replaceIfNotNull(request.loginHint, getParameter(OIDCLoginProtocol.LOGIN_HINT_PARAM));
            request.bindingMessage = replaceIfNotNull(request.bindingMessage, getParameter(CibaGrantType.BINDING_MESSAGE));
            request.userCode = replaceIfNotNull(request.userCode, getParameter(CibaGrantType.USER_CODE));
            request.requestedExpiry = replaceIfNotNull(request.requestedExpiry, getIntParameter(CibaGrantType.REQUESTED_EXPIRY));
    
            request.prompt = replaceIfNotNull(request.prompt, getParameter(OIDCLoginProtocol.PROMPT_PARAM));
            request.nonce = replaceIfNotNull(request.nonce, getParameter(OIDCLoginProtocol.NONCE_PARAM));
            request.maxAge = replaceIfNotNull(request.maxAge, getIntParameter(OIDCLoginProtocol.MAX_AGE_PARAM));
            request.uiLocales = replaceIfNotNull(request.uiLocales, getParameter(OIDCLoginProtocol.UI_LOCALES_PARAM));
            request.claims = replaceIfNotNull(request.claims, getParameter(OIDCLoginProtocol.CLAIMS_PARAM));
    
            extractAdditionalReqParams(request.additionalReqParams);
        }
