public class keycloak_0148 {

        public static XMLEvent bypassElementBlock(XMLEventReader xmlEventReader) throws ParsingException {
            XMLEvent xmlEvent;
            int levelOfNesting = 0;
            if (! xmlEventReader.hasNext()) {
                return null;
            }
    
            try {
                do {
                    xmlEvent = xmlEventReader.nextEvent();
                    if (xmlEvent instanceof StartElement) {
                        levelOfNesting++;
                    } else if (xmlEvent instanceof EndElement) {
                        levelOfNesting--;
                    }
                } while (levelOfNesting > 0 && xmlEventReader.hasNext());
            } catch (XMLStreamException e) {
                throw logger.parserException(e);
            }
    
            return xmlEvent;
        }
}
