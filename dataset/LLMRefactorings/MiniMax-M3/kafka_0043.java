public class kafka_0043 {

        public String toString(boolean lineBreaks) {
            TreeMap<Short, String> apiKeysText = new TreeMap<>();
            for (ApiVersion supportedVersion : this.supportedVersions.values())
                apiKeysText.put(supportedVersion.apiKey(), apiVersionToText(supportedVersion));
            for (ApiVersion apiVersion : unknownApis)
                apiKeysText.put(apiVersion.apiKey(), apiVersionToText(apiVersion));
    
            addUnsupportedApiKeys(apiKeysText);
    
            String separator = lineBreaks ? ",\n\t" : ", ";
            StringBuilder bld = new StringBuilder();
            bld.append("(");
            if (lineBreaks)
                bld.append("\n\t");
            bld.append(String.join(separator, apiKeysText.values()));
            if (lineBreaks)
                bld.append("\n");
            bld.append(")");
            return bld.toString();
        }
    
        private void addUnsupportedApiKeys(TreeMap<Short, String> apiKeysText) {
            for (ApiKeys apiKey : ApiKeys.clientApis()) {
                if (!apiKeysText.containsKey(apiKey.id)) {
                    String entry = apiKey.name + "(" +
                            apiKey.id + "): " + "UNSUPPORTED";
                    apiKeysText.put(apiKey.id, entry);
                }
            }
        }
}
