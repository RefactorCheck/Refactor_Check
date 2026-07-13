public class arthas_0141 {

        public static InputStream loadInputRcFile() {
            InputStream inputrc;
            // step 1: load custom keymap file
            try {
                String customInputrc = System.getProperty("user.home") + "/.arthas/conf/inputrc";
                inputrc = new FileInputStream(customInputrc);
                logger.info("Loaded custom keymap file from " + customInputrc);
                return inputrc;
            } catch (Throwable e) {
                // ignore
            }
    
            // step 2: load arthas default keymap file
            inputrc = TermServer.class.getClassLoader().getResourceAsStream(ShellServerOptions.DEFAULT_INPUTRC);
            if (inputrc != null) {
                logger.info("Loaded arthas keymap file from " + ShellServerOptions.DEFAULT_INPUTRC);
                return inputrc;
            }
    
            // step 3: fall back to termd default keymap file
            inputrc = Keymap.class.getResourceAsStream("inputrc");
            if (inputrc != null) {
                return inputrc;
            }
    
            throw new IllegalStateException("Could not load inputrc file.");
        }
}
