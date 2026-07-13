public class arthas_0141 {

        public static InputStream loadInputRcFile() {
            InputStream inputrc = loadCustomInputRc();
            if (inputrc != null) {
                return inputrc;
            }

            inputrc = loadArthasDefaultInputRc();
            if (inputrc != null) {
                return inputrc;
            }

            inputrc = loadTermdDefaultInputRc();
            if (inputrc != null) {
                return inputrc;
            }

            throw new IllegalStateException("Could not load inputrc file.");
        }

        private static InputStream loadCustomInputRc() {
            try {
                String customInputrc = System.getProperty("user.home") + "/.arthas/conf/inputrc";
                InputStream inputrc = new FileInputStream(customInputrc);
                logger.info("Loaded custom keymap file from " + customInputrc);
                return inputrc;
            } catch (Throwable e) {
                return null;
            }
        }

        private static InputStream loadArthasDefaultInputRc() {
            InputStream inputrc = TermServer.class.getClassLoader().getResourceAsStream(ShellServerOptions.DEFAULT_INPUTRC);
            if (inputrc != null) {
                logger.info("Loaded arthas keymap file from " + ShellServerOptions.DEFAULT_INPUTRC);
            }
            return inputrc;
        }

        private static InputStream loadTermdDefaultInputRc() {
            return Keymap.class.getResourceAsStream("inputrc");
        }
}
