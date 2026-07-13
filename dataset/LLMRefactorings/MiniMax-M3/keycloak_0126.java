public class keycloak_0126 {

        public KeyWrapper cloneKey() {
            KeyWrapper key = new KeyWrapper();
            copyKeyProperties(key);
            return key;
        }

        private void copyKeyProperties(KeyWrapper key) {
            key.providerId = this.providerId;
            key.providerPriority = this.providerPriority;
            key.kid = this.kid;
            key.algorithm = this.algorithm;
            key.type = this.type;
            key.use = this.use;
            key.status = this.status;
            key.secretKey = this.secretKey;
            key.publicKey = this.publicKey;
            key.privateKey = this.privateKey;
            key.certificate = this.certificate;
            key.curve = this.curve;
            if (this.certificateChain != null) {
                key.certificateChain = new ArrayList<>(this.certificateChain);
            }
            key.isDefaultClientCertificate = this.isDefaultClientCertificate;
        }
}
