public class dubbo_0019 {

        public static String sign(Object[] parameters, String metadata, String key) throws RuntimeException {
            if (parameters == null) {
                return sign(metadata, key);
            }
            validateParameters(parameters);
            Object[] includeMetadata = new Object[parameters.length + 1];
            System.arraycopy(parameters, 0, includeMetadata, 0, parameters.length);
            includeMetadata[parameters.length] = metadata;
            byte[] includeMetadataBytes;
            try {
                includeMetadataBytes = toByteArray(includeMetadata);
            } catch (IOException e) {
                throw new RuntimeException("Failed to generate HMAC: " + e.getMessage());
            }
            return sign(includeMetadataBytes, key);
        }

        private static void validateParameters(Object[] parameters) {
            for (int i = 0; i < parameters.length; i++) {
                if (!(parameters[i] instanceof Serializable)) {
                    throw new IllegalArgumentException("The parameter [" + i + "] to be signed was not serializable.");
                }
            }
        }
}
