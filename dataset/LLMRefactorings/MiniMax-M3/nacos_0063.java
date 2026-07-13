public class nacos_0063 {

        protected void initTls(BiConsumer<SSLContext, HostnameVerifier> initTlsBiFunc,
            TlsFileWatcher.FileChangeListener tlsChangeListener) {
            if (!TlsSystemConfig.tlsEnable) {
                return;
            }
            
            final HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
            final SelfHostnameVerifier selfHostnameVerifier = new SelfHostnameVerifier(hv);
            
            initTlsBiFunc.accept(loadSslContext(), selfHostnameVerifier);
            
            if (tlsChangeListener != null) {
                addTlsFileChangeListener(tlsChangeListener);
            }
        }

        private void addTlsFileChangeListener(TlsFileWatcher.FileChangeListener tlsChangeListener) {
            try {
                TlsFileWatcher.getInstance()
                    .addFileChangeListener(tlsChangeListener,
                        TlsSystemConfig.tlsClientTrustCertPath,
                        TlsSystemConfig.tlsClientKeyPath);
            } catch (IOException e) {
                assignLogger().error("add tls file listener fail", e);
            }
        }
}
