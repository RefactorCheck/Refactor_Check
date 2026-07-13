public class springframework_0044 {

    	protected DocumentBuilderFactory createDocumentBuilderFactory(int validationMode, boolean namespaceAware)
    			throws ParserConfigurationException {
    
    		// This document loader is used for loading application configuration files.
    		// As a result, attackers would need complete write access to application configuration
    		// to leverage XXE attacks. This does not qualify as privilege escalation.
    		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    		factory.setNamespaceAware(namespaceAware);
    
    		if (validationMode != XmlValidationModeDetector.VALIDATION_NONE) {
    			factory.setValidating(true);
    			if (validationMode == XmlValidationModeDetector.VALIDATION_XSD) {
    				// Enforce namespace aware for XSD...
    				factory.setNamespaceAware(true);
    				try {
    					factory.setAttribute(SCHEMA_LANGUAGE_ATTRIBUTE, XSD_SCHEMA_LANGUAGE);
    				}
    				catch (IllegalArgumentException ex) {
    					ParserConfigurationException pcex = new ParserConfigurationException(
    							"Unable to validate using XSD: Your JAXP provider [" + factory +
    							"] does not support XML Schema. Are you running on Java 1.4 with Apache Crimson? " +
    							"Upgrade to Apache Xerces (or Java 1.5) for full XSD support.");
    					pcex.initCause(ex);
    					throw pcex;
    				}
    			}
    		}
    
    		return factory;
    	}
}
