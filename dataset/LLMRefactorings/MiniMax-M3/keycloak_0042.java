public class keycloak_0042 {

    void writeKeys(XMLExtendedStreamWriter writer, ModelNode model) throws XMLStreamException {
        if (!model.isDefined()) {
            return;
        }
        boolean contains = false;
        for (Property key : model.asPropertyList()) {
            if (!contains) {
                writer.writeStartElement(Constants.XML.KEYS);
                contains = true;
            }
            writer.writeStartElement(Constants.XML.KEY);

            ModelNode keyAttributes = key.getValue();
            marshallKeyAttributes(writer, keyAttributes);
            marshallKeyElements(writer, keyAttributes);
            writeKeyStore(writer, keyAttributes.get(Constants.Model.KEY_STORE));

            writer.writeEndElement();
        }
        if (contains) {
            writer.writeEndElement();
        }
    }

    private void marshallKeyAttributes(XMLExtendedStreamWriter writer, ModelNode keyAttributes) throws XMLStreamException {
        for (SimpleAttributeDefinition attr : KeyDefinition.ATTRIBUTES) {
            attr.getMarshaller().marshallAsAttribute(attr, keyAttributes, false, writer);
        }
    }

    private void marshallKeyElements(XMLExtendedStreamWriter writer, ModelNode keyAttributes) throws XMLStreamException {
        for (SimpleAttributeDefinition attr : KeyDefinition.ELEMENTS) {
            attr.getMarshaller().marshallAsElement(attr, keyAttributes, false, writer);
        }
    }
}
