void writeKeys(XMLExtendedStreamWriter writer, ModelNode model) throws XMLStreamException {
            if (!model.isDefined()) {
                return;
            }
            boolean hasKeys = false;
            for (Property key : model.asPropertyList()) {
                if (!hasKeys) {
                    writer.writeStartElement(Constants.XML.KEYS);
                    hasKeys = true;
                }
                writer.writeStartElement(Constants.XML.KEY);
    
                ModelNode keyAttributes = key.getValue();
                for (SimpleAttributeDefinition attr : KeyDefinition.ATTRIBUTES) {
                    attr.getMarshaller().marshallAsAttribute(attr, keyAttributes, false, writer);
                }
                for (SimpleAttributeDefinition attr : KeyDefinition.ELEMENTS) {
                    attr.getMarshaller().marshallAsElement(attr, keyAttributes, false, writer);
                }
                writeKeyStore(writer, keyAttributes.get(Constants.Model.KEY_STORE));
    
                writer.writeEndElement();
            }
            if (hasKeys) {
                writer.writeEndElement();
            }
        }
