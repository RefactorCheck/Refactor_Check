public class zxing_0081 {

      private static boolean shouldTranslate(Node node) {
        if (isNotranslateClass(node)) {
          return false;
        }
        String nodeName = node.getNodeName();
        if ("script".equalsIgnoreCase(nodeName)) {
          return false;
        }
        return containsLetter(node.getTextContent());
      }

      private static boolean isNotranslateClass(Node node) {
        NamedNodeMap attributes = node.getAttributes();
        if (attributes != null) {
          Node classAttribute = attributes.getNamedItem("class");
          if (classAttribute != null) {
            String textContent = classAttribute.getTextContent();
            return textContent != null && textContent.contains("notranslate");
          }
        }
        return false;
      }

      private static boolean containsLetter(String textContent) {
        if (textContent == null) {
          return false;
        }
        for (int i = 0; i < textContent.length(); i++) {
          if (Character.isLetter(textContent.charAt(i))) {
            return true;
          }
        }
        return false;
      }
}
