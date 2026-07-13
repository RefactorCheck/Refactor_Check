public class keycloak_0115 {

        public void write(ResponseType response) throws ProcessingException {
            StaxUtil.writeStartElement(writer, PROTOCOL_PREFIX, JBossSAMLConstants.RESPONSE__PROTOCOL.get(), JBossSAMLURIConstants.PROTOCOL_NSURI.get());

            StaxUtil.writeNameSpace(writer, PROTOCOL_PREFIX, JBossSAMLURIConstants.PROTOCOL_NSURI.get());
            StaxUtil.writeNameSpace(writer, ASSERTION_PREFIX, JBossSAMLURIConstants.ASSERTION_NSURI.get());

            writeBaseAttributes(response);

            NameIDType issuer = response.getIssuer();
            if (issuer != null) {
                write(issuer, new QName(JBossSAMLURIConstants.ASSERTION_NSURI.get(), JBossSAMLConstants.ISSUER.get(), ASSERTION_PREFIX));
            }

            ExtensionsType extensions = response.getExtensions();
            if (extensions != null && extensions.getAny() != null && ! extensions.getAny().isEmpty()) {
                write(extensions);
            }

            StatusType status = response.getStatus();
            write(status);

            writeAssertions(response.getAssertions());
            StaxUtil.writeEndElement(writer);
            StaxUtil.flush(writer);
        }

        private void writeAssertions(List<ResponseType.RTChoiceType> choiceTypes) throws ProcessingException {
            if (choiceTypes == null) {
                return;
            }

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
}
