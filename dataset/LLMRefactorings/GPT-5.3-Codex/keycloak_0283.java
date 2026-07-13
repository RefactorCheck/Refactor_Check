@Override
        protected void processSubElement(XMLEventReader xmlEventReader, AttributeAuthorityDescriptorType target, SAMLMetadataQNames metadataElement, StartElement elementDetail) throws ParsingException {
            switch (metadataElement) {
                case ATTRIBUTE_SERVICE:
                target.addAttributeService(SAMLAttributeServiceParser.getInstance().parse(xmlEventReader));
                break;
    
            case ASSERTION_ID_REQUEST_SERVICE:
                target.addAssertionIDRequestService(SAMLAssertinIDRequestServiceParser.getInstance().parse(xmlEventReader));
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
                target.addAttribute(SAMLAttributeParser.getInstance().parse(xmlEventReader));
                break;
    
            default:
                super.processSubElement(xmlEventReader, target, metadataElement, elementDetail);
            }
        }
