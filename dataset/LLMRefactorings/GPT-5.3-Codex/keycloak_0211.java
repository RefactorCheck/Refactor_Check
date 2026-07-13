@Override
        public void executeOnEvent(ClientPolicyContext context) throws ClientPolicyException {
            HttpRequest httpRequest = session.getContext().getHttpRequest();
            switch (context.getEvent()) {
                case REGISTER:
                case UPDATE:
                    ClientCRUDContext clientUpdateContext = (ClientCRUDContext)context;
                    autoConfigure(clientUpdateContext.getProposedClientRepresentation());
                    validate(clientUpdateContext.getProposedClientRepresentation());
                    break;
                case TOKEN_REQUEST:
                case SERVICE_ACCOUNT_TOKEN_REQUEST:
                case BACKCHANNEL_TOKEN_REQUEST:
                    AccessToken.Confirmation certConf = MtlsHoKTokenUtil.bindTokenWithClientCertificate(httpRequest, session);
                    if (certConf == null) {
                        throw new ClientPolicyException(OAuthErrorException.INVALID_REQUEST, "Client Certification missing for MTLS HoK Token Binding");
                    }
                    break;
                case TOKEN_REFRESH:
                    checkTokenRefresh((TokenRefreshContext) context, httpRequest);
                    break;
                case TOKEN_REVOKE:
                    checkTokenRevoke((TokenRevokeContext) context, httpRequest);
                    break;
                case USERINFO_REQUEST:
                    checkUserInfo((UserInfoRequestContext) context, httpRequest);
                    break;
                case LOGOUT_REQUEST:
                    checkLogout((LogoutRequestContext) context, httpRequest);
                    break;
                default:
                    return;
            }
        }
