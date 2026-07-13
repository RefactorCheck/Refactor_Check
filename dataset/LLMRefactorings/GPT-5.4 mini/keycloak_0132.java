public class keycloak_0132 {

        private void verifySafeHtml() {
            PropertyResourceBundle bundle = getPropertyResourceBundle();
    
            PropertyResourceBundle bundleEnglish;
            String englishFilePath = file.getAbsolutePath().replaceAll("resources-community", "resources")
                    .replaceAll("_[a-zA-Z-_]*\\.properties", "_en.properties");
            try (FileInputStream fis = new FileInputStream(englishFilePath)) {
                bundleEnglish = new PropertyResourceBundle(fis);
            } catch (IOException e) {
                throw new RuntimeException("unable to read file " + englishFilePath, e);
            }
    
            bundle.getKeys().asIterator().forEachRemaining(key -> {
                String value = bundle.getString(key);
                value = normalizeValue(key, value);
                String englishValue = getEnglishValue(key, bundleEnglish);
                englishValue = normalizeValue(key, englishValue);
    
                value = santizeAnchors(key, value, englishValue);
    
                // Only if the English source string contains HTML we also allow HTML in the translation
                PolicyFactory policy = containsHtml(englishValue) ? POLICY_SOME_HTML : POLICY_NO_HTML;
                String sanitizedValue = policy.sanitize(value);
    
                // Sanitizer will escape HTML entities for quotes and also for numberic tags like '<1>'
                sanitizedValue = org.apache.commons.text.StringEscapeUtils.unescapeHtml4(sanitizedValue);
                // Sanitizer will add them when there are double curly braces
                sanitizedValue = sanitizedValue.replace("<!-- -->", "");
    
                if (!Objects.equals(sanitizedValue, value)) {
    
                    // Strip identical characters from the beginning and the end to show where the difference is
                    int start = 0;
                    while (start < sanitizedValue.length() && start < value.length() && value.charAt(start) == sanitizedValue.charAt(start)) {
                        start++;
                    }
                    int end = 0;
                    while (end < sanitizedValue.length() - start && end < value.length() - start && value.charAt(value.length() - end - 1) == sanitizedValue.charAt(sanitizedValue.length() - end - 1)) {
                        end++;
                    }
    
                    messages.add("Illegal HTML in key " + key + " for file " + file + ": '" + value.substring(start, value.length() - end) + "' vs. '" + sanitizedValue.substring(start, sanitizedValue.length() - end) + "'");
                }
    
            });
        }
}
