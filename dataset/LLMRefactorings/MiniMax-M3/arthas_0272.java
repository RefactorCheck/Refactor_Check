public class arthas_0272 {

        private String checkPropertyName(String name) {
            // Check name as-is
            if (containsKey(name)) {
                return name;
            }
            // Check name with just dots replaced
            String noDotName = name.replace('.', '_');
            if (checkTransformedName(name, noDotName)) {
                return noDotName;
            }
            // Check name with just hyphens replaced
            String noHyphenName = name.replace('-', '_');
            if (checkTransformedName(name, noHyphenName)) {
                return noHyphenName;
            }
            // Check name with dots and hyphens replaced
            String noDotNoHyphenName = noDotName.replace('-', '_');
            if (checkTransformedName(noDotName, noDotNoHyphenName)) {
                return noDotNoHyphenName;
            }
            // Give up
            return null;
        }

        private boolean checkTransformedName(String original, String transformed) {
            return !original.equals(transformed) && containsKey(transformed);
        }
}
