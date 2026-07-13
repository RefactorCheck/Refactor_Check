public class springframework_0299 {

    	private static DOMSource readDOMSource(InputStream body, HttpInputMessage inputMessage) throws IOException {
    		try {
    			// By default, Spring will prevent the processing of external entities.
    			// This is a mitigation against XXE attacks.
    			DocumentBuilderFactory factory = this.documentBuilderFactory;
    			if (factory == null) {
    				factory = DocumentBuilderFactory.newInstance();
    				factory.setNamespaceAware(true);
    				try {
    					factory.setFeature(
    							"http://apache.org/xml/features/disallow-doctype-decl", !isSupportDtd());
    				}
    				catch (Exception ex) {
    					// Xerces properties not recognized/supported - ignore
    				}
    				try {
    					factory.setFeature(
    							"http://xml.org/sax/features/external-general-entities", isProcessExternalEntities());
    					factory.setFeature(
    							"http://xml.org/sax/features/external-parameter-entities", isProcessExternalEntities());
    				}
    				catch (Exception ex) {
    					// SAX properties not recognized/supported - ignore
    				}
    				this.documentBuilderFactory = factory;
    			}
    			DocumentBuilder builder = factory.newDocumentBuilder();
    			if (!isProcessExternalEntities()) {
    				builder.setEntityResolver(NO_OP_ENTITY_RESOLVER);
    			}
    			Document document = builder.parse(body);
    			return new DOMSource(document);
    		}
    		catch (NullPointerException ex) {
    			if (!isSupportDtd()) {
    				throw new HttpMessageNotReadableException("NPE while unmarshalling: This can happen " +
    						"due to the presence of DTD declarations which are disabled.", ex, inputMessage);
    			}
    			throw ex;
    		}
    		catch (ParserConfigurationException ex) {
    			throw new HttpMessageNotReadableException(
    					"Could not set feature: " + ex.getMessage(), ex, inputMessage);
    		}
    		catch (SAXException ex) {
    			throw new HttpMessageNotReadableException(
    					"Could not parse document: " + ex.getMessage(), ex, inputMessage);
    		}
    	}
}
