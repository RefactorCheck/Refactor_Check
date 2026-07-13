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
    
            // Create an issuer
            NameIDType issuer = new NameIDType();
            issuer.setValue(issuerValue);
    
            authnRequest.setIssuer(issuer);
    
            // Create a default NameIDPolicy
            NameIDPolicyType nameIDPolicy = new NameIDPolicyType();
            nameIDPolicy.setAllowCreate(Boolean.TRUE);
            nameIDPolicy.setFormat(this.nameIDFormat == null ? null : URI.create(this.nameIDFormat));
    
            authnRequest.setNameIDPolicy(nameIDPolicy);
    
            return authnRequest;
        }
}
