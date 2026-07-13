public class springframework_0044 {

    private static final String XSD_VALIDATION_ERROR_MESSAGE =
            "Unable to validate using XSD: Your JAXP provider [%s] does not support XML Schema. " +
            "Are you running on Java 1.4 with Apache Crimson? " +
            "Upgrade to Apache Xerces (or Java 1.5) for full XSD support.";

    protected DocumentBuilderFactory createDocumentBuilderFactory(int validationMode, boolean namespaceAware)
            throws ParserConfigurationException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(namespaceAware);

        if (validationMode != XmlValidationModeDetector.VALIDATION_NONE) {
            factory.setValidating(true);
            if (validationMode == XmlValidationModeDetector.VALIDATION_XSD) {
                factory.setNamespaceAware(true);
                try {
                    factory.setAttribute(SCHEMA_LANGUAGE_ATTRIBUTE, XSD_SCHEMA_LANGUAGE);
                }
                catch (IllegalArgumentException ex) {
                    ParserConfigurationException pcex = new ParserConfigurationException(
                            String.format(XSD_VALIDATION_ERROR_MESSAGE, factory));
                    pcex.initCause(ex);
                    throw pcex;
                }
            }
        }

        return factory;
    }
}
