public class keycloak_0011 {

        public void write(LogoutRequestType logOutRequest) throws ProcessingException {
            StaxUtil.writeStartElement(writer, PROTOCOL_PREFIX, JBossSAMLConstants.LOGOUT_REQUEST.get(), PROTOCOL_NSURI.get());

            StaxUtil.writeNameSpace(writer, PROTOCOL_PREFIX, PROTOCOL_NSURI.get());
            StaxUtil.writeNameSpace(writer, ASSERTION_PREFIX, ASSERTION_NSURI.get());
            StaxUtil.writeDefaultNameSpace(writer, ASSERTION_NSURI.get());

            writeAttributes(logOutRequest);

            NameIDType issuer = logOutRequest.getIssuer();
            write(issuer, new QName(ASSERTION_NSURI.get(), JBossSAMLConstants.ISSUER.get(), ASSERTION_PREFIX));

            writeSignature(logOutRequest.getSignature());

            writeExtensions(logOutRequest.getExtensions());

            writeNameID(logOutRequest.getNameID());

            writeSessionIndexes(logOutRequest.getSessionIndex());

            StaxUtil.writeEndElement(writer);
            StaxUtil.flush(writer);
        }

        private void writeAttributes(LogoutRequestType logOutRequest) {
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
        }

        private void writeSignature(Element signature) {
            if (signature != null) {
                StaxUtil.writeDOMElement(writer, signature);
            }
        }

        private void writeExtensions(ExtensionsType extensions) {
            if (extensions != null && !extensions.getAny().isEmpty()) {
                write(extensions);
            }
        }

        private void writeNameID(NameIDType nameID) {
            if (nameID != null) {
                write(nameID, new QName(ASSERTION_NSURI.get(), JBossSAMLConstants.NAMEID.get(), ASSERTION_PREFIX));
            }
        }

        private void writeSessionIndexes(List<String> sessionIndexes) {
            for (String sessionIndex : sessionIndexes) {
                StaxUtil.writeStartElement(writer, PROTOCOL_PREFIX, JBossSAMLConstants.SESSION_INDEX.get(), PROTOCOL_NSURI.get());
                StaxUtil.writeCharacters(writer, sessionIndex);
                StaxUtil.writeEndElement(writer);
                StaxUtil.flush(writer);
            }
        }
}
