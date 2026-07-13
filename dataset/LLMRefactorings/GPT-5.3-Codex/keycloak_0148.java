public static XMLEvent bypassElementBlock(XMLEventReader xmlEventReader) throws ParsingException {
            XMLEvent xmlEvent;
            int nestingDepth = 0;
            if (! xmlEventReader.hasNext()) {
                return null;
            }
    
            try {
                do {
                    xmlEvent = xmlEventReader.nextEvent();
                    if (xmlEvent instanceof StartElement) {
                        nestingDepth++;
                    } else if (xmlEvent instanceof EndElement) {
                        nestingDepth--;
                    }
                } while (nestingDepth > 0 && xmlEventReader.hasNext());
            } catch (XMLStreamException e) {
                throw logger.parserException(e);
            }
    
            return xmlEvent;
        }
