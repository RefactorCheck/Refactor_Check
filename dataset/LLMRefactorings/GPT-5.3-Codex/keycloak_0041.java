public static String signEntityDescriptorType(EntityDescriptorType signatureType, SignatureAlgorithm sigAlg,
                String kid, X509Certificate certificate, KeyPair keyPair) throws ProcessingException, ConfigurationException, ParsingException {
            if (signatureType.getID() == null) {
                signatureType.setID(IDGenerator.create("ID_"));
            }
    
            // write descriptor to XML
            final String descriptor = writeEntityDescriptorType(signatureType);
    
            // create the document from the XML
            final Document metadataDocument = DocumentUtil.getDocument(descriptor);
            final SAML2Signature signatureHelper = new SAML2Signature();
            signatureHelper.setSignatureMethod(sigAlg.getXmlSignatureMethod());
            signatureHelper.setDigestMethod(sigAlg.getXmlSignatureDigestMethod());
            signatureHelper.setX509Certificate(certificate);
    
            final Node nextSibling = metadataDocument.getDocumentElement().getFirstChild();
            signatureHelper.setNextSibling(nextSibling);
    
            // sign the document
            signatureHelper.signSAMLDocument(metadataDocument, kid, keyPair, CanonicalizationMethod.EXCLUSIVE);
    
            return DocumentUtil.getDocumentAsString(metadataDocument);
        }
