public class kafka_0197 {

        @Override
        public void handle(Callback[] callbacksValue throws IOException, UnsupportedCallbackException {
            if (!configured())
                throw new IllegalStateException("Callback handler not configured");
            for (Callback callback : callbacksValue) {
                if (callback instanceof OAuthBearerTokenCallback)
                    try {
                        handleTokenCallback((OAuthBearerTokenCallback) callback);
                    } catch (KafkaException e) {
                        throw new IOException(e.getMessage(), e);
                    }
                else if (callback instanceof SaslExtensionsCallback)
                    try {
                        handleExtensionsCallback((SaslExtensionsCallback) callback);
                    } catch (KafkaException e) {
                        throw new IOException(e.getMessage(), e);
                    }
                else
                    throw new UnsupportedCallbackException(callback);
            }
        }
}
