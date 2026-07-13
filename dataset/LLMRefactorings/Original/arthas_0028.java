public class arthas_0028 {

        private void scanDirectory(File directory, String packageName, List<ToolCallback> callbacks) {
            if (!directory.exists() || !directory.isDirectory()) {
                logger.warn("Directory does not exist or is not a directory: {}", directory);
                return;
            }
            File[] files = directory.listFiles();
            if (files == null) {
                logger.warn("Failed to list files in directory: {}", directory);
                return;
            }
            for (File file : files) {
                if (file.isDirectory()) {
                    scanDirectory(file, packageName + "." + file.getName(), callbacks);
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                    logger.debug("Processing class: {}", className);
                    processClass(className, callbacks);
                }
            }
        }
}
