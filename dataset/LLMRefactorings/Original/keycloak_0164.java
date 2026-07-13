public class keycloak_0164 {

        public static SAML11ConditionsType parseSAML11Conditions(XMLEventReader xmlEventReader) throws ParsingException {
            StartElement startElement;
            SAML11ConditionsType conditions = new SAML11ConditionsType();
            StartElement conditionsElement = StaxParserUtil.getNextStartElement(xmlEventReader);
            StaxParserUtil.validate(conditionsElement, JBossSAMLConstants.CONDITIONS.get());
    
            String assertionNS = SAML11Constants.ASSERTION_11_NSURI;
    
            QName notBeforeQName = new QName("", JBossSAMLConstants.NOT_BEFORE.get());
            QName notBeforeQNameWithNS = new QName(assertionNS, JBossSAMLConstants.NOT_BEFORE.get());
    
            QName notAfterQName = new QName("", JBossSAMLConstants.NOT_ON_OR_AFTER.get());
            QName notAfterQNameWithNS = new QName(assertionNS, JBossSAMLConstants.NOT_ON_OR_AFTER.get());
    
            Attribute notBeforeAttribute = conditionsElement.getAttributeByName(notBeforeQName);
            if (notBeforeAttribute == null)
                notBeforeAttribute = conditionsElement.getAttributeByName(notBeforeQNameWithNS);
    
            Attribute notAfterAttribute = conditionsElement.getAttributeByName(notAfterQName);
            if (notAfterAttribute == null)
                notAfterAttribute = conditionsElement.getAttributeByName(notAfterQNameWithNS);
    
            if (notBeforeAttribute != null) {
                String notBeforeValue = StaxParserUtil.getAttributeValue(notBeforeAttribute);
                conditions.setNotBefore(XMLTimeUtil.parse(notBeforeValue));
            }
    
            if (notAfterAttribute != null) {
                String notAfterValue = StaxParserUtil.getAttributeValue(notAfterAttribute);
                conditions.setNotOnOrAfter(XMLTimeUtil.parse(notAfterValue));
            }
    
            while (xmlEventReader.hasNext()) {
                XMLEvent xmlEvent = StaxParserUtil.peek(xmlEventReader);
                if (xmlEvent instanceof EndElement) {
                    EndElement end = StaxParserUtil.getNextEndElement(xmlEventReader);
                    if (StaxParserUtil.matches(end, JBossSAMLConstants.CONDITIONS.get()))
                        break;
                }
                startElement = StaxParserUtil.peekNextStartElement(xmlEventReader);
                if (startElement == null)
                    break;
                String tag = StaxParserUtil.getElementName(startElement);
    
                if (SAML11Constants.AUDIENCE_RESTRICTION_CONDITION.equals(tag)) {
                    startElement = StaxParserUtil.getNextStartElement(xmlEventReader);
                    SAML11AudienceRestrictionCondition restrictCond = new SAML11AudienceRestrictionCondition();
    
                    startElement = StaxParserUtil.getNextStartElement(xmlEventReader);
                    if (StaxParserUtil.getElementName(startElement).equals(JBossSAMLConstants.AUDIENCE.get())) {
                        restrictCond.add(URI.create(StaxParserUtil.getElementText(xmlEventReader)));
                    }
                    EndElement theEndElement = StaxParserUtil.getNextEndElement(xmlEventReader);
                    StaxParserUtil.validate(theEndElement, SAML11Constants.AUDIENCE_RESTRICTION_CONDITION);
                    conditions.add(restrictCond);
                } else
                    throw logger.parserUnknownTag(tag, startElement.getLocation());
            }
            return conditions;
        }
}
