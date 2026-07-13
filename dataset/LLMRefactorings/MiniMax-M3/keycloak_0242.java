public class keycloak_0242 {

    @Override
    protected void sendAuthnRequest(HttpFacade httpFacade, SAML2AuthnRequestBuilder authnRequestBuilder, BaseSAML2BindingBuilder binding) {
        try {
            SOAPMessage message = createSoapMessage();
            SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
            addNamespaceDeclarations(envelope);
            createPaosRequestHeader(envelope);
            createEcpRequestHeader(envelope);
            SOAPBody body = envelope.getBody();
            body.addDocument(binding.postBinding(authnRequestBuilder.toDocument()).getDocument());
            message.writeTo(httpFacade.getResponse().getOutputStream());
        } catch (Exception e) {
            throw new RuntimeException("Could not create AuthnRequest.", e);
        }
    }

    private SOAPMessage createSoapMessage() throws SOAPException {
        MessageFactory messageFactory = MessageFactory.newInstance();
        return messageFactory.createMessage();
    }

    private void addNamespaceDeclarations(SOAPEnvelope envelope) throws SOAPException {
        envelope.addNamespaceDeclaration(NS_PREFIX_SAML_ASSERTION, JBossSAMLURIConstants.ASSERTION_NSURI.get());
        envelope.addNamespaceDeclaration(NS_PREFIX_SAML_PROTOCOL, JBossSAMLURIConstants.PROTOCOL_NSURI.get());
        envelope.addNamespaceDeclaration(NS_PREFIX_PAOS_BINDING, JBossSAMLURIConstants.PAOS_BINDING.get());
        envelope.addNamespaceDeclaration(NS_PREFIX_PROFILE_ECP, JBossSAMLURIConstants.ECP_PROFILE.get());
    }
}
