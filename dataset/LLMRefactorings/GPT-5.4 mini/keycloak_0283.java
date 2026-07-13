public class keycloak_0283 {

        @Override
        protected void processSubElement(XMLEventReader xmlEventReader, AttributeAuthorityDescriptorType target, SAMLMetadataQNames element, StartElement elementDetail) throws ParsingException {
            switch (element) {
                case ATTRIBUTE_SERVICE:
                target.addAttributeService(SAMLAttributeServiceParser.getInstance().parse(xmlEventReader));
                break;

            case ASSERTION_ID_REQUEST_SERVICE:
                target.addAssertionIDRequestService(SAMLAssertinIDRequestServiceParser.getInstance().parse(xmlEventReader));
                break;

            case NAMEID_FORMAT:
                target.addNameIDFormat(readElementText(xmlEventReader));
                break;

            case ATTRIBUTE_PROFILE:
                target.addAttributeProfile(readElementText(xmlEventReader));
                break;

            case ATTRIBUTE:
                target.addAttribute(SAMLAttributeParser.getInstance().parse(xmlEventReader));
                break;

            default:
                super.processSubElement(xmlEventReader, target, element, elementDetail);
            }
        }

        private String readElementText(XMLEventReader xmlEventReader) throws ParsingException {
            StaxParserUtil.advance(xmlEventReader);
            return StaxParserUtil.getElementText(xmlEventReader);
        }
}
