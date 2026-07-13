public class keycloak_0211 {

        @Override
        public void executeOnEvent(ClientPolicyContext context) throws ClientPolicyException {
            HttpRequest request = session.getContext().getHttpRequest();
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
                    bindTokenWithClientCertificate(request);
                    break;
                case TOKEN_REFRESH:
                    checkTokenRefresh((TokenRefreshContext) context, request);
                    break;
                case TOKEN_REVOKE:
                    checkTokenRevoke((TokenRevokeContext) context, request);
                    break;
                case USERINFO_REQUEST:
                    checkUserInfo((UserInfoRequestContext) context, request);
                    break;
                case LOGOUT_REQUEST:
                    checkLogout((LogoutRequestContext) context, request);
                    break;
                default:
                    return;
            }
        }

        private void bindTokenWithClientCertificate(HttpRequest request) throws ClientPolicyException {
            AccessToken.Confirmation certConf = MtlsHoKTokenUtil.bindTokenWithClientCertificate(request, session);
            if (certConf == null) {
                throw new ClientPolicyException(OAuthErrorException.INVALID_REQUEST, "Client Certification missing for MTLS HoK Token Binding");
            }
        }
}
