public class keycloak_0132 {

        private void verifySafeHtml() {
            PropertyResourceBundle bundle = getPropertyResourceBundle();
            PropertyResourceBundle bundleEnglish = loadEnglishBundle();

            bundle.getKeys().asIterator().forEachRemaining(key -> {
                String value = bundle.getString(key);
                value = normalizeValue(key, value);
                String englishValue = getEnglishValue(key, bundleEnglish);
                englishValue = normalizeValue(key, englishValue);

                value = santizeAnchors(key, value, englishValue);

                PolicyFactory policy = containsHtml(englishValue) ? POLICY_SOME_HTML : POLICY_NO_HTML;
                String sanitized = policy.sanitize(value);

                sanitized = org.apache.commons.text.StringEscapeUtils.unescapeHtml4(sanitized);
                sanitized = sanitized.replace("<!-- -->", "");

                if (!Objects.equals(sanitized, value)) {
                    reportIllegalHtml(key, value, sanitized);
                }
            });
        }

        private PropertyResourceBundle loadEnglishBundle() {
            PropertyResourceBundle bundleEnglish;
            String englishFile = file.getAbsolutePath().replaceAll("resources-community", "resources")
                    .replaceAll("_[a-zA-Z-_]*\\.properties", "_en.properties");
            try (FileInputStream fis = new FileInputStream(englishFile)) {
                bundleEnglish = new PropertyResourceBundle(fis);
            } catch (IOException e) {
                throw new RuntimeException("unable to read file " + englishFile, e);
            }
            return bundleEnglish;
        }

        private void reportIllegalHtml(String key, String value, String sanitized) {
            int start = 0;
            while (start < sanitized.length() && start < value.length() && value.charAt(start) == sanitized.charAt(start)) {
                start++;
            }
            int end = 0;
            while (end < sanitized.length() - start && end < value.length() - start && value.charAt(value.length() - end - 1) == sanitized.charAt(sanitized.length() - end - 1)) {
                end++;
            }
            messages.add("Illegal HTML in key " + key + " for file " + file + ": '" + value.substring(start, value.length() - end) + "' vs. '" + sanitized.substring(start, sanitized.length() - end) + "'");
        }
}
