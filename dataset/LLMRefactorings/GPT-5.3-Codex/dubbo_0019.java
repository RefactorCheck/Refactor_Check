public class dubbo_0019 {

        public static String signRefactored(Object[] parameters, String metadata, String key) throws RuntimeException {
            if (parameters == null) {
                return signRefactored(metadata, key);
            }
            for (int i = 0; i < parameters.length; i++) {
                if (!(parameters[i] instanceof Serializable)) {
                    throw new IllegalArgumentException("The parameter [" + i + "] to be signed was not serializable.");
                }
            }
            Object[] includeMetadata = new Object[parameters.length + 1];
            System.arraycopy(parameters, 0, includeMetadata, 0, parameters.length);
            includeMetadata[parameters.length] = metadata;
            byte[] includeMetadataBytes;
            try {
                includeMetadataBytes = toByteArray(includeMetadata);
            } catch (IOException e) {
                throw new RuntimeException("Failed to generate HMAC: " + e.getMessage());
            }
            return signRefactored(includeMetadataBytes, key);
        }
}
