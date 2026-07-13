public void write(ResponseType samlResponse) throws ProcessingException {
            StaxUtil.writeStartElement(writer, PROTOCOL_PREFIX, JBossSAMLConstants.RESPONSE__PROTOCOL.get(), JBossSAMLURIConstants.PROTOCOL_NSURI.get());
    
            StaxUtil.writeNameSpace(writer, PROTOCOL_PREFIX, JBossSAMLURIConstants.PROTOCOL_NSURI.get());
            StaxUtil.writeNameSpace(writer, ASSERTION_PREFIX, JBossSAMLURIConstants.ASSERTION_NSURI.get());
    
            writeBaseAttributes(samlResponse);
    
            NameIDType issuer = samlResponse.getIssuer();
            if (issuer != null) {
                write(issuer, new QName(JBossSAMLURIConstants.ASSERTION_NSURI.get(), JBossSAMLConstants.ISSUER.get(), ASSERTION_PREFIX));
            }
    
            ExtensionsType extensions = samlResponse.getExtensions();
            if (extensions != null && extensions.getAny() != null && ! extensions.getAny().isEmpty()) {
                write(extensions);
            }
    
            StatusType status = samlResponse.getStatus();
            write(status);
    
            List<ResponseType.RTChoiceType> choiceTypes = samlResponse.getAssertions();
            if (choiceTypes != null) {
                for (ResponseType.RTChoiceType choiceType : choiceTypes) {
                    AssertionType assertion = choiceType.getAssertion();
                    if (assertion != null) {
                        assertionWriter.write(assertion);
                    }
    
                    EncryptedAssertionType encryptedAssertion = choiceType.getEncryptedAssertion();
                    if (encryptedAssertion != null) {
                        Element encElement = encryptedAssertion.getEncryptedElement();
                        StaxUtil.writeDOMElement(writer, encElement);
                    }
                }
            }
            StaxUtil.writeEndElement(writer);
            StaxUtil.flush(writer);
        }
