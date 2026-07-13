public class zxing_0081 {

      private static boolean shouldTranslate(Node node) {
        // Ignore "notranslate" nodes
        NamedNodeMap attributes = node.getAttributes();
        if (attributes != null) {
          Node classAttribute = attributes.getNamedItem("class");
          if (classAttribute != null) {
            String textContent = classAttribute.getTextContent();
            if (textContent != null && textContent.contains("notranslate")) {
              return false;
            }
          }
        }
        String nodeName = node.getNodeName();
        if ("script".equalsIgnoreCase(nodeName)) {
          return false;
        }
        // Ignore non-text snippets
        String textContent = node.getTextContent();
        if (textContent != null) {
          for (int i = 0; i < textContent.length(); i++) {
            if (Character.isLetter(textContent.charAt(i))) {
              return true;
            }
          }
        }
        return false;
      }
}
