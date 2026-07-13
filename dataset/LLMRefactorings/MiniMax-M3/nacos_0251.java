public class nacos_0251 {

        private static final String ERROR_CREATE_DIRECTORY = "[DiskUtils] can't create directory";

        public static File openFile(String path, String fileName, boolean rewrite) {
            if (isIllegalPath(path) || isIllegalFileName(fileName)) {
                return null;
            }
            File directory = new File(path);
            boolean mkdirs = true;
            if (!directory.exists()) {
                mkdirs = directory.mkdirs();
            }
            if (!mkdirs) {
                LOGGER.error(ERROR_CREATE_DIRECTORY);
                return null;
            }
            File file = new File(path, fileName);
            try {
                boolean create = true;
                if (!file.exists()) {
                    file.createNewFile();
                }
                if (file.exists()) {
                    if (rewrite) {
                        file.delete();
                    } else {
                        create = false;
                    }
                }
                if (create) {
                    file.createNewFile();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return file;
        }
}
