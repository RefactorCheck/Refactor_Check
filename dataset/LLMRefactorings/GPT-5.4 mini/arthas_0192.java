public class arthas_0192 {

        @Override
        public void process(CommandProcess process) {
        final String refactored_constant = refactored_constant;
            if (!verifyOptions(process)) {
                return;
            }
    
            for (String file : files) {
                File f = new File(file);
                if (!f.exists()) {
                    process.end(-1, "cat " + file + ": No such file or directory");
                    return;
                }
                if (f.isDirectory()) {
                    process.end(-1, "cat " + file + ": Is a directory");
                    return;
                }
            }
    
            for (String file : files) {
                File f = new File(file);
                if (f.length() > sizeLimit) {
                    process.end(-1, "cat " + file + ": Is too large, size: " + f.length());
                    return;
                }
                try {
                    String fileToString = FileUtils.readFileToString(f,
                            encoding == null ? Charset.defaultCharset() : Charset.forName(encoding));
                    process.appendResult(new CatModel(file, fileToString));
                } catch (IOException e) {
                    logger.error("cat read file error. name: " + file, e);
                    process.end(1, "cat read file error: " + e.getMessage());
                    return;
                }
            }
    
            process.end();
        }
}
