public class keycloak_0216 {

        @Override
        public Object parse(XMLEventReader xmlEventReader) throws ParsingException {
            StartElement element = StaxParserUtil.getNextStartElement(xmlEventReader);
            StaxParserUtil.validate(element, SAMLAssertionQNames.ATTRIBUTE_VALUE.getQName());
    
            Attribute nil = element.getAttributeByName(NIL);
            if (nil != null) {
                String nilValue = StaxParserUtil.getAttributeValue(nil);
                if (nilValue != null && (nilValue.equalsIgnoreCase("true") || nilValue.equals("1"))) {
                    String elementText = StaxParserUtil.getElementText(xmlEventReader);
                    if (elementText == null || elementText.isEmpty()) {
                        return null;
                    } else {
                        throw logger.nullValueError("nil attribute is not in SAML20 format");
                    }
                } else {
                    throw logger.parserRequiredAttribute(JBossSAMLURIConstants.XSI_PREFIX.get() + ":nil");
                }
            }
    
            Attribute type = element.getAttributeByName(XSI_TYPE);
            if (type == null) {
                if (StaxParserUtil.hasTextAhead(xmlEventReader)) {
                    return StaxParserUtil.getElementText(xmlEventReader);
                }
                // Else we may have Child Element
                XMLEvent xmlEvent = StaxParserUtil.peek(xmlEventReader);
                if (xmlEvent instanceof StartElement) {
                    element = (StartElement) xmlEvent;
                    final QName qName = element.getName();
                    if (Objects.equals(qName, SAMLAssertionQNames.NAMEID.getQName())) {
                        return SAMLParserUtil.parseNameIDType(xmlEventReader);
                    }
                } else if (xmlEvent instanceof EndElement) {
                    return "";
                }
    
                // when no type attribute assigned -> assume anyType
                return parseAsString(xmlEventReader);
            }
    
            //      RK Added an additional type check for base64Binary type as calheers is passing this type
            String typeValue = StaxParserUtil.getAttributeValue(type);
            if (typeValue.contains(":string")) {
                return StaxParserUtil.getElementText(xmlEventReader);
            } else if (typeValue.contains(":anyType")) {
                return parseAsString(xmlEventReader);
            } else if(typeValue.contains(":base64Binary")){
                return StaxParserUtil.getElementText(xmlEventReader);
            } else if(typeValue.contains(":date")){
                return XMLTimeUtil.parse(StaxParserUtil.getElementText(xmlEventReader));
            } else if(typeValue.contains(":boolean")){
                return StaxParserUtil.getElementText(xmlEventReader);
            }
    
            return parseAsString(xmlEventReader);
        }
}
