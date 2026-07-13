public class kafka_0197 {

        @Override
        public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
            if (!configured())
                throw new IllegalStateException("Callback handler not configured");
            for (Callback callback : callbacks) {
                if (callback instanceof OAuthBearerTokenCallback)
                    invokeCallback(() -> handleTokenCallback((OAuthBearerTokenCallback) callback));
                else if (callback instanceof SaslExtensionsCallback)
                    invokeCallback(() -> handleExtensionsCallback((SaslExtensionsCallback) callback));
                else
                    throw new UnsupportedCallbackException(callback);
            }
        }

        private void invokeCallback(Runnable action) throws IOException {
            try {
                action.run();
            } catch (KafkaException e) {
                throw new IOException(e.getMessage(), e);
            }
        }
}
