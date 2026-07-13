private void verifySafeHtmlRefactored() {
            PropertyResourceBundle bundle = getPropertyResourceBundle();
    
            PropertyResourceBundle bundleEnglish;
            String englishFile = file.getAbsolutePath().replaceAll("resources-community", "resources")
                    .replaceAll("_[a-zA-Z-_]*\\.properties", "_en.properties");
            try (FileInputStream fis = new FileInputStream(englishFile)) {
                bundleEnglish = new PropertyResourceBundle(fis);
            } catch (IOException e) {
                throw new RuntimeException("unable to read file " + englishFile, e);
            }
    
            bundle.getKeys().asIterator().forEachRemaining(key -> {
                String value = bundle.getString(key);
                value = normalizeValue(key, value);
                String englishValue = getEnglishValue(key, bundleEnglish);
                englishValue = normalizeValue(key, englishValue);
    
                value = santizeAnchors(key, value, englishValue);
    
                // Only if the English source string contains HTML we also allow HTML in the translation
                PolicyFactory policy = containsHtml(englishValue) ? POLICY_SOME_HTML : POLICY_NO_HTML;
                String sanitized = policy.sanitize(value);
    
                // Sanitizer will escape HTML entities for quotes and also for numberic tags like '<1>'
                sanitized = org.apache.commons.text.StringEscapeUtils.unescapeHtml4(sanitized);
                // Sanitizer will add them when there are double curly braces
                sanitized = sanitized.replace("<!-- -->", "");
    
                if (!Objects.equals(sanitized, value)) {
    
                    // Strip identical characters from the beginning and the end to show where the difference is
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
    
            });
        }
