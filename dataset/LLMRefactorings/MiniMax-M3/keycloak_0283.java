public class keycloak_0283 {

        @Override
        protected void processSubElement(XMLEventReader xmlEventReader, AttributeAuthorityDescriptorType target, SAMLMetadataQNames element, StartElement elementDetail) throws ParsingException {
            switch (element) {
                case ATTRIBUTE_SERVICE:
                    addAttributeService(xmlEventReader, target);
                    break;

                case ASSERTION_ID_REQUEST_SERVICE:
                    addAssertionIDRequestService(xmlEventReader, target);
                    break;

                case NAMEID_FORMAT:
                    StaxParserUtil.advance(xmlEventReader);
                    target.addNameIDFormat(StaxParserUtil.getElementText(xmlEventReader));
                    break;

                case ATTRIBUTE_PROFILE:
                    StaxParserUtil.advance(xmlEventReader);
                    target.addAttributeProfile(StaxParserUtil.getElementText(xmlEventReader));
                    break;

                case ATTRIBUTE:
                    addAttribute(xmlEventReader, target);
                    break;

                default:
                    super.processSubElement(xmlEventReader, target, element, elementDetail);
            }
        }

        private void addAttributeService(XMLEventReader xmlEventReader, AttributeAuthorityDescriptorType target) {
            target.addAttributeService(SAMLAttributeServiceParser.getInstance().parse(xmlEventReader));
        }

        private void addAssertionIDRequestService(XMLEventReader xmlEventReader, AttributeAuthorityDescriptorType target) {
            target.addAssertionIDRequestService(SAMLAssertinIDRequestServiceParser.getInstance().parse(xmlEventReader));
        }

        private void addAttribute(XMLEventReader xmlEventReader, AttributeAuthorityDescriptorType target) {
            target.addAttribute(SAMLAttributeParser.getInstance().parse(xmlEventReader));
        }
}
