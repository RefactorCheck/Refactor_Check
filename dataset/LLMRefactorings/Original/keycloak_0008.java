public class keycloak_0008 {

        @Override
        protected void processSubElement(XMLEventReader xmlEventReader, SubjectConfirmationType target, SAMLAssertionQNames element, StartElement elementDetail) throws ParsingException {
            switch (element) {
                case NAMEID:
                    NameIDType nameID = SAMLParserUtil.parseNameIDType(xmlEventReader);
                    target.setNameID(nameID);
                    break;
    
                case ENCRYPTED_ID:
                    Element domElement = StaxParserUtil.getDOMElement(xmlEventReader);
                    target.setEncryptedID(new EncryptedElementType(domElement));
                    break;
    
                case SUBJECT_CONFIRMATION_DATA:
                    SubjectConfirmationDataType subjectConfirmationData = SAMLSubjectConfirmationDataParser.INSTANCE.parse(xmlEventReader);
                    target.setSubjectConfirmationData(subjectConfirmationData);
                    break;
    
                default:
                    throw LOGGER.parserUnknownTag(StaxParserUtil.getElementName(elementDetail), elementDetail.getLocation());
            }
        }
}
