public class keycloak_0043 {

        private static XMLInputFactory getXMLInputFactory() {
            boolean tcclJaxp = SystemPropertiesUtil.getSystemProperty(GeneralConstants.TCCL_JAXP, "false")
                    .equalsIgnoreCase("true");
            ClassLoader previousTCCL = SecurityActions.getTCCL();
            try {
                if (tcclJaxp) {
                    SecurityActions.setTCCL(StaxParserUtil.class.getClassLoader());
                }
                XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
    
                xmlInputFactory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.TRUE);
                xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
                xmlInputFactory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.TRUE);
                xmlInputFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.TRUE);
                xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
    
                return xmlInputFactory;
            } finally {
                if (tcclJaxp) {
                    SecurityActions.setTCCL(previousTCCL);
                }
            }
        }
}
