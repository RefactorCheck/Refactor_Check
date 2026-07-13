public class arthas_0138 {

        private static final int MAX_SIZE_LIMIT_OF_NON_TTY = 128 * 1024;

        private boolean verifyOptions(CommandProcess process) {
            if(this.file == null && this.input == null) {
                process.end(-1);
                return false;
            }
    
            if (sizeLimit > MAX_SIZE_LIMIT) {
                process.end(-1, "sizeLimit cannot be large than: " + MAX_SIZE_LIMIT);
                return false;
            }
    
            if (!process.session().isTty() && sizeLimit > MAX_SIZE_LIMIT_OF_NON_TTY) {
                process.end(-1,
                        "When executing in non-tty session, sizeLimit cannot be large than: " + MAX_SIZE_LIMIT_OF_NON_TTY);
                return false;
            }
            return true;
        }
}
