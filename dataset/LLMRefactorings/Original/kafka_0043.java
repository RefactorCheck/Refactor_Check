public class kafka_0043 {

        public String toString(boolean lineBreaks) {
            // The apiVersion collection may not be in sorted order.  We put it into
            // a TreeMap before printing it out to ensure that we always print in
            // ascending order.
            TreeMap<Short, String> apiKeysText = new TreeMap<>();
            for (ApiVersion supportedVersion : this.supportedVersions.values())
                apiKeysText.put(supportedVersion.apiKey(), apiVersionToText(supportedVersion));
            for (ApiVersion apiVersion : unknownApis)
                apiKeysText.put(apiVersion.apiKey(), apiVersionToText(apiVersion));
    
            // Also handle the case where some apiKey types are not specified at all in the given ApiVersions,
            // which may happen when the remote is too old.
            for (ApiKeys apiKey : ApiKeys.clientApis()) {
                if (!apiKeysText.containsKey(apiKey.id)) {
                    String bld = apiKey.name + "(" +
                            apiKey.id + "): " + "UNSUPPORTED";
                    apiKeysText.put(apiKey.id, bld);
                }
            }
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
}
