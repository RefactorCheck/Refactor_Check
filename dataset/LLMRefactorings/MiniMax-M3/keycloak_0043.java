public class keycloak_0043 {

        private static XMLInputFactory getXMLInputFactory() {
            boolean tccl_jaxp = SystemPropertiesUtil.getSystemProperty(GeneralConstants.TCCL_JAXP, "false")
                    .equalsIgnoreCase("true");
            ClassLoader prevTCCL = SecurityActions.getTCCL();
            try {
                if (tccl_jaxp) {
                    SecurityActions.setTCCL(StaxParserUtil.class.getClassLoader());
                }
                XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
                configureXMLInputFactory(xmlInputFactory);
                return xmlInputFactory;
            } finally {
                if (tccl_jaxp) {
                    SecurityActions.setTCCL(prevTCCL);
                }
            }
        }

        private static void configureXMLInputFactory(XMLInputFactory xmlInputFactory) {
            xmlInputFactory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.TRUE);
            xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
            xmlInputFactory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.TRUE);
            xmlInputFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.TRUE);
            xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        }
}
