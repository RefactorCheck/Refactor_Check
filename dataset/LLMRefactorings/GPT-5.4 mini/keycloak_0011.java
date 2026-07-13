public class keycloak_0011 {

        public void write(LogoutRequestType logOutRequest) throws ProcessingException {
            String protocolNamespace = PROTOCOL_NSURI.get();
            String assertionNamespace = ASSERTION_NSURI.get();

            StaxUtil.writeStartElement(writer, PROTOCOL_PREFIX, JBossSAMLConstants.LOGOUT_REQUEST.get(), protocolNamespace);

            StaxUtil.writeNameSpace(writer, PROTOCOL_PREFIX, protocolNamespace);
            StaxUtil.writeNameSpace(writer, ASSERTION_PREFIX, assertionNamespace);
            StaxUtil.writeDefaultNameSpace(writer, assertionNamespace);

            // Attributes
            StaxUtil.writeAttribute(writer, JBossSAMLConstants.ID.get(), logOutRequest.getID());
            StaxUtil.writeAttribute(writer, JBossSAMLConstants.VERSION.get(), logOutRequest.getVersion());
            StaxUtil.writeAttribute(writer, JBossSAMLConstants.ISSUE_INSTANT.get(), logOutRequest.getIssueInstant().toString());

            URI destination = logOutRequest.getDestination();
            if (destination != null) {
                StaxUtil.writeAttribute(writer, JBossSAMLConstants.DESTINATION.get(), destination.toASCIIString());
            }

            String consent = logOutRequest.getConsent();
            if (StringUtil.isNotNull(consent))
                StaxUtil.writeAttribute(writer, JBossSAMLConstants.CONSENT.get(), consent);

            NameIDType issuer = logOutRequest.getIssuer();
            write(issuer, new QName(assertionNamespace, JBossSAMLConstants.ISSUER.get(), ASSERTION_PREFIX));

            Element signature = logOutRequest.getSignature();
            if (signature != null) {
                StaxUtil.writeDOMElement(writer, signature);
            }

            ExtensionsType extensions = logOutRequest.getExtensions();
            if (extensions != null && ! extensions.getAny().isEmpty()) {
                write(extensions);
            }

            NameIDType nameID = logOutRequest.getNameID();
            if (nameID != null) {
                write(nameID, new QName(assertionNamespace, JBossSAMLConstants.NAMEID.get(), ASSERTION_PREFIX));
            }

            List<String> sessionIndexes = logOutRequest.getSessionIndex();

            for (String sessionIndex : sessionIndexes) {
                StaxUtil.writeStartElement(writer, PROTOCOL_PREFIX, JBossSAMLConstants.SESSION_INDEX.get(), PROTOCOL_NSURI.get());

                StaxUtil.writeCharacters(writer, sessionIndex);

                StaxUtil.writeEndElement(writer);
                StaxUtil.flush(writer);
            }

            StaxUtil.writeEndElement(writer);
            StaxUtil.flush(writer);
        }
}
