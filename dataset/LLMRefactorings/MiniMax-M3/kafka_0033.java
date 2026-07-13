public class kafka_0033 {

    private static final String PEM_PASSWORD_ERROR_MESSAGE = "SSL key store password cannot be specified with PEM format, only key password may be specified";

    protected SecurityStore createKeystore(String type, String path, Password password, Password keyPassword, Password privateKey, Password certificateChain) {
        if (privateKey != null) {
            if (!PEM_TYPE.equals(type))
                throw new InvalidConfigurationException("SSL private key can be specified only for PEM, but key store type is " + type + ".");
            else if (certificateChain == null)
                throw new InvalidConfigurationException("SSL private key is specified, but certificate chain is not specified.");
            else if (path != null)
                throw new InvalidConfigurationException("Both SSL key store location and separate private key are specified.");
            else if (password != null)
                throw new InvalidConfigurationException(PEM_PASSWORD_ERROR_MESSAGE + ".");
            else
                return new PemStore(certificateChain, privateKey, keyPassword);
        } else if (certificateChain != null) {
            throw new InvalidConfigurationException("SSL certificate chain is specified, but private key is not specified");
        } else if (PEM_TYPE.equals(type) && path != null) {
            if (password != null)
                throw new InvalidConfigurationException(PEM_PASSWORD_ERROR_MESSAGE);
            else
                return new FileBasedPemStore(path, keyPassword, true);
        } else if (path == null && password != null) {
            throw new InvalidConfigurationException("SSL key store is not specified, but key store password is specified.");
        } else if (path != null && password == null) {
            throw new InvalidConfigurationException("SSL key store is specified, but key store password is not specified.");
        } else if (path != null && password != null) {
            return new FileBasedStore(type, path, password, keyPassword, true);
        } else
            return null;
    }
}
