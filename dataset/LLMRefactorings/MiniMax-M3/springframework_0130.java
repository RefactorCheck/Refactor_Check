public class springframework_0130 {

    protected ResourceBundle doGetBundle(String basename, Locale locale) throws MissingResourceException {
        ClassLoader classLoader = getBundleClassLoader();
        Assert.state(classLoader != null, "No bundle ClassLoader set");

        MessageSourceControl control = this.control;
        if (control != null) {
            try {
                return ResourceBundle.getBundle(basename, locale, classLoader, control);
            }
            catch (UnsupportedOperationException ex) {
                // Probably in a Java Module System environment on JDK 9+
                this.control = null;
                handleUnsupportedControlOperation(ex);
            }
        }

        // Fallback: plain getBundle lookup without Control handle
        return ResourceBundle.getBundle(basename, locale, classLoader);
    }

    private void handleUnsupportedControlOperation(UnsupportedOperationException ex) {
        Charset charset = getDefaultCharset();
        if (charset != null && logger.isInfoEnabled()) {
            logger.info("ResourceBundleMessageSource is configured to read resources with encoding '" +
                    charset + "' but ResourceBundle.Control is not supported in current system environment: " +
                    ex.getMessage() + " - falling back to plain ResourceBundle.getBundle retrieval with the " +
                    "platform default encoding. Consider setting the 'defaultCharset' property to 'null' " +
                    "for participating in the platform default and therefore avoiding this log message.");
        }
    }
}
