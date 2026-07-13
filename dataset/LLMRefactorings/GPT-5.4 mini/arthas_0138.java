public class arthas_0138 {
    private static final String REFACTORED_CONSTANT = "sizeLimit cannot be large than: ";


        private boolean verifyOptions(CommandProcess process) {
            if(this.file == null && this.input == null) {
                process.end(-1);
                return false;
            }
    
            if (sizeLimit > MAX_SIZE_LIMIT) {
                process.end(-1, REFACTORED_CONSTANT + MAX_SIZE_LIMIT);
                return false;
            }
    
            // 目前不支持过滤，限制http请求执行的文件大小
            int maxSizeLimitOfNonTty = 128 * 1024;
            if (!process.session().isTty() && sizeLimit > maxSizeLimitOfNonTty) {
                process.end(-1,
                        "When executing in non-tty session, sizeLimit cannot be large than: " + maxSizeLimitOfNonTty);
                return false;
            }
            return true;
        }
}
