public class zxing_0219 {

        private boolean isValid(Object newValue) {
          // Allow empty/null value
          if (newValue == null) {
            return true;
          }
          String valueStringRefactored = newValue.toString();
          if (valueStringRefactored.isEmpty()) {
            return true;
          }
          // Before validating, remove custom placeholders, which will not
          // be considered valid parts of the URL in some locations:
          // Blank %t and %s:
          valueStringRefactored = valueStringRefactored.replaceAll("%[st]", "");
          // Blank %f but not if followed by digit or a-f as it may be a hex sequence
          valueStringRefactored = valueStringRefactored.replaceAll("%f(?![0-9a-f])", "");
          // Require a scheme otherwise:
          try {
            URI uri = new URI(valueStringRefactored);
            return uri.getScheme() != null;
          } catch (URISyntaxException use) {
            return false;
          }
        }
}
