public class keycloak_0203 {

        public MultivaluedHashMap<String, KeyInfo> parse(InputStream stream) throws ParsingException {
            MultivaluedHashMap<String, KeyInfo> res = new MultivaluedHashMap<>();
    
            try {
                DocumentBuilder builder = DocumentUtil.getDocumentBuilder();
                Document doc = builder.parse(stream);
    
                XPathExpression expr = xpath.compile("//m:EntityDescriptor/m:IDPSSODescriptor/m:KeyDescriptor");
                NodeList keyDescriptors = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
                for (int i = 0; i < keyDescriptors.getLength(); i ++) {
                    Node keyDescriptor = keyDescriptors.item(i);
                    Element keyDescriptorEl = (Element) keyDescriptor;
                    KeyInfo ki = processKeyDescriptor(keyDescriptorEl);
                    if (ki != null) {
                        String use = keyDescriptorEl.getAttribute(JBossSAMLConstants.USE.get());
                        res.add(use, ki);
                    }
                }
            } catch (SAXException | IOException | ParserConfigurationException | MarshalException | XPathExpressionException e) {
                throw new ParsingException("Error parsing SAML descriptor", e);
            }
    
            return res;
        }
}
