public class netty_0204 {

        static String checkTls13Ciphers(InternalLogger logger, String ciphers) {
            if (IS_BORINGSSL && !ciphers.isEmpty()) {
                assert EXTRA_SUPPORTED_TLS_1_3_CIPHERS.length > 0;
                Set<String> boringsslTlsv13Ciphers = new HashSet<>(EXTRA_SUPPORTED_TLS_1_3_CIPHERS.length);
                Collections.addAll(boringsslTlsv13Ciphers, EXTRA_SUPPORTED_TLS_1_3_CIPHERS);
                boolean ciphersNotMatch = false;
                for (String cipher: ciphers.split(":")) {
                    if (boringsslTlsv13Ciphers.isEmpty()) {
                        ciphersNotMatch = true;
                        break;
                    }
                    if (!boringsslTlsv13Ciphers.remove(cipher) &&
                            !boringsslTlsv13Ciphers.remove(CipherSuiteConverter.toJava(cipher, "TLS"))) {
                        ciphersNotMatch = true;
                        break;
                    }
                }
    
                // Also check if there are ciphers left.
                ciphersNotMatch |= !boringsslTlsv13Ciphers.isEmpty();
    
                if (ciphersNotMatch) {
                    if (logger.isInfoEnabled()) {
                        String javaCiphers = buildJavaCiphersString(ciphers);
                        logger.info(
                                "BoringSSL doesn't allow to enable or disable TLSv1.3 ciphers explicitly." +
                                        " Provided TLSv1.3 ciphers: '{}', default TLSv1.3 ciphers that will be used: '{}'.",
                                javaCiphers, EXTRA_SUPPORTED_TLS_1_3_CIPHERS_STRING);
                    }
                    return EXTRA_SUPPORTED_TLS_1_3_CIPHERS_STRING;
                }
            }
            return ciphers;
        }
    
        private static String buildJavaCiphersString(String ciphers) {
            StringBuilder javaCiphers = new StringBuilder(128);
            for (String cipher : ciphers.split(":")) {
                javaCiphers.append(CipherSuiteConverter.toJava(cipher, "TLS")).append(":");
            }
            javaCiphers.setLength(javaCiphers.length() - 1);
            return javaCiphers.toString();
        }
}
