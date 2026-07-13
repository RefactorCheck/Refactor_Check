public class keycloak_0041 {

        public static String signEntityDescriptorType(EntityDescriptorType type, SignatureAlgorithm sigAlg,
                String kid, X509Certificate certificate, KeyPair keyPair) throws ProcessingException, ConfigurationException, ParsingException {
            if (type.getID() == null) {
                type.setID(IDGenerator.create("ID_"));
            }

            final String descriptor = writeEntityDescriptorType(type);
            final Document metadataDocument = DocumentUtil.getDocument(descriptor);

            configureSignatureHelper(metadataDocument, sigAlg, certificate).signSAMLDocument(metadataDocument, kid, keyPair, CanonicalizationMethod.EXCLUSIVE);

            return DocumentUtil.getDocumentAsString(metadataDocument);
        }

        private static SAML2Signature configureSignatureHelper(Document metadataDocument, SignatureAlgorithm sigAlg,
                X509Certificate certificate) {
            final SAML2Signature signatureHelper = new SAML2Signature();
            signatureHelper.setSignatureMethod(sigAlg.getXmlSignatureMethod());
            signatureHelper.setDigestMethod(sigAlg.getXmlSignatureDigestMethod());
            signatureHelper.setX509Certificate(certificate);

            final Node nextSibling = metadataDocument.getDocumentElement().getFirstChild();
            signatureHelper.setNextSibling(nextSibling);

            return signatureHelper;
        }
}
