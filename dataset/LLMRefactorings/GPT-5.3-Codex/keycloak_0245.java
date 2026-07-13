public AuthnRequestType createAuthnRequestType(String id, String assertionConsumerURL, String destination,
                                                       String issuerValue, URI protocolBinding) throws ConfigurationException {
            XMLGregorianCalendar issueInstant = XMLTimeUtil.getIssueInstant();
    
            AuthnRequestType authenticationRequest = new AuthnRequestType(id, issueInstant);
            authenticationRequest.setAssertionConsumerServiceURL(URI.create(assertionConsumerURL));
            authenticationRequest.setProtocolBinding(protocolBinding);
            if (destination != null) {
                authenticationRequest.setDestination(URI.create(destination));
            }
    
            // Create an issuer
            NameIDType issuer = new NameIDType();
            issuer.setValue(issuerValue);
    
            authenticationRequest.setIssuer(issuer);
    
            // Create a default NameIDPolicy
            NameIDPolicyType nameIDPolicy = new NameIDPolicyType();
            nameIDPolicy.setAllowCreate(Boolean.TRUE);
            nameIDPolicy.setFormat(this.nameIDFormat == null ? null : URI.create(this.nameIDFormat));
    
            authenticationRequest.setNameIDPolicy(nameIDPolicy);
    
            return authenticationRequest;
        }
