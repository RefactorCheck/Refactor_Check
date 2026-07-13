public void write(LogoutRequestType logoutRequest) throws ProcessingException {
            StaxUtil.writeStartElement(writer, PROTOCOL_PREFIX, JBossSAMLConstants.LOGOUT_REQUEST.get(), PROTOCOL_NSURI.get());
    
            StaxUtil.writeNameSpace(writer, PROTOCOL_PREFIX, PROTOCOL_NSURI.get());
            StaxUtil.writeNameSpace(writer, ASSERTION_PREFIX, ASSERTION_NSURI.get());
            StaxUtil.writeDefaultNameSpace(writer, ASSERTION_NSURI.get());
    
            // Attributes
            StaxUtil.writeAttribute(writer, JBossSAMLConstants.ID.get(), logoutRequest.getID());
            StaxUtil.writeAttribute(writer, JBossSAMLConstants.VERSION.get(), logoutRequest.getVersion());
            StaxUtil.writeAttribute(writer, JBossSAMLConstants.ISSUE_INSTANT.get(), logoutRequest.getIssueInstant().toString());
    
            URI destination = logoutRequest.getDestination();
            if (destination != null) {
                StaxUtil.writeAttribute(writer, JBossSAMLConstants.DESTINATION.get(), destination.toASCIIString());
            }
    
            String consent = logoutRequest.getConsent();
            if (StringUtil.isNotNull(consent))
                StaxUtil.writeAttribute(writer, JBossSAMLConstants.CONSENT.get(), consent);
    
            NameIDType issuer = logoutRequest.getIssuer();
            write(issuer, new QName(ASSERTION_NSURI.get(), JBossSAMLConstants.ISSUER.get(), ASSERTION_PREFIX));
    
            Element signature = logoutRequest.getSignature();
            if (signature != null) {
                StaxUtil.writeDOMElement(writer, signature);
            }
    
            ExtensionsType extensions = logoutRequest.getExtensions();
            if (extensions != null && ! extensions.getAny().isEmpty()) {
                write(extensions);
            }
    
            NameIDType nameID = logoutRequest.getNameID();
            if (nameID != null) {
                write(nameID, new QName(ASSERTION_NSURI.get(), JBossSAMLConstants.NAMEID.get(), ASSERTION_PREFIX));
            }
    
            List<String> sessionIndexes = logoutRequest.getSessionIndex();
    
            for (String sessionIndex : sessionIndexes) {
                StaxUtil.writeStartElement(writer, PROTOCOL_PREFIX, JBossSAMLConstants.SESSION_INDEX.get(), PROTOCOL_NSURI.get());
    
                StaxUtil.writeCharacters(writer, sessionIndex);
    
                StaxUtil.writeEndElement(writer);
                StaxUtil.flush(writer);
            }
    
            StaxUtil.writeEndElement(writer);
            StaxUtil.flush(writer);
        }
