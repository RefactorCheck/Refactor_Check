public class keycloak_0093 {

            private BloomFilter<String> loadDenylistFromPlaintext() {
                try {
                    LOG.infof("Loading denylist start: name=%s path=%s", name, path);
                    long loadStartMillis = System.currentTimeMillis();
    
                    long passwordCount = countPasswordsInDenylistFile();
                    double fpp = getFalsePositiveProbability();
    
                    BloomFilter<String> filter = BloomFilter.create(
                            Funnels.stringFunnel(StandardCharsets.UTF_8),
                            passwordCount,
                            fpp);
    
                    insertPasswordsInto(filter);
    
                    double expectedFfp = filter.expectedFpp();
                    long loadTimeMillis = System.currentTimeMillis() - loadStartMillis;
                    LOG.infof("Loading denylist finished: name=%s passwords=%s path=%s falsePositiveProbability=%s expectedFalsePositiveProbability=%s loadTime=%dms",
                            name, passwordCount, path, fpp, expectedFfp, loadTimeMillis);
    
                    return filter;
                } catch (IOException e) {
                    throw new RuntimeException("Loading denylist failed: Could not load password denylist path=" + path, e);
                }
            }
}
