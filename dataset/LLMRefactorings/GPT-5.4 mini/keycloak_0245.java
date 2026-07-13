public class keycloak_0245 {

        public AuthnRequestType createAuthnRequestType(String id, String assertionConsumerURL, String destination,
                                                       String issuerValue, URI protocolBinding) throws ConfigurationException {
            XMLGregorianCalendar issueInstant = XMLTimeUtil.getIssueInstant();

            AuthnRequestType authnRequest = new AuthnRequestType(id, issueInstant);
            authnRequest.setAssertionConsumerServiceURL(URI.create(assertionConsumerURL));
            authnRequest.setProtocolBinding(protocolBinding);
            if (destination != null) {
                authnRequest.setDestination(URI.create(destination));
            }

            authnRequest.setIssuer(createIssuer(issuerValue));
            authnRequest.setNameIDPolicy(createDefaultNameIDPolicy());

            return authnRequest;
        }

        private NameIDType createIssuer(String issuerValue) {
            NameIDType issuer = new NameIDType();
            issuer.setValue(issuerValue);
            return issuer;
        }

        private NameIDPolicyType createDefaultNameIDPolicy() {
            NameIDPolicyType nameIDPolicy = new NameIDPolicyType();
            nameIDPolicy.setAllowCreate(Boolean.TRUE);
            nameIDPolicy.setFormat(this.nameIDFormat == null ? null : URI.create(this.nameIDFormat));
            return nameIDPolicy;
        }
}
