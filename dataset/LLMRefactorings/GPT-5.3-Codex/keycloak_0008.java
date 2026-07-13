@Override
        protected void processSubElement(XMLEventReader xmlEventReader, SubjectConfirmationType target, SAMLAssertionQNames targetElement, StartElement elementDetail) throws ParsingException {
            switch (targetElement) {
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
