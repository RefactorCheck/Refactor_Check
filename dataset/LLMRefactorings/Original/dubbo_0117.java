public class dubbo_0117 {

        @Override
        @SuppressWarnings({"unchecked"})
        public <T> T copy(URL url, Object src, Class<T> targetClass, Type type) {
            // TODO: maybe we have better way to do this
            if (src != null && ProtobufUtils.isProtobufClass(src.getClass())) {
                return (T) src;
            }
            Serialization serialization = url.getOrDefaultFrameworkModel()
                    .getExtensionLoader(Serialization.class)
                    .getExtension(UrlUtils.serializationOrDefault(url));
    
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                ObjectOutput objectOutput = serialization.serialize(url, outputStream);
                objectOutput.writeObject(src);
                objectOutput.flushBuffer();
    
                try (ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray())) {
                    ObjectInput objectInput = serialization.deserialize(url, inputStream);
                    if (type != null) {
                        return objectInput.readObject(targetClass, type);
                    } else {
                        return objectInput.readObject(targetClass);
                    }
                } catch (ClassNotFoundException | IOException e) {
                    logger.error(PROTOCOL_ERROR_DESERIALIZE, "", "", "Unable to deep copy parameter to target class.", e);
                }
    
            } catch (Throwable e) {
                logger.error(PROTOCOL_ERROR_DESERIALIZE, "", "", "Unable to deep copy parameter to target class.", e);
            }
    
            if (src.getClass().equals(targetClass)) {
                return (T) src;
            } else {
                return null;
            }
        }
}
