public class arthas_0139 {

    private void dumpClassIfNecessary(Class<?> clazz, byte[] data) {
        String className = clazz.getName();
        ClassLoader classLoader = clazz.getClassLoader();

        File dumpDir = dumpDir();
        if (!dumpDir.mkdirs() && !dumpDir.exists()) {
            logger.warn("create dump directory:{} failed.", dumpDir.getAbsolutePath());
            return;
        }

        String fileName = buildDumpFileName(classLoader, className);

        File dumpClassFile = new File(dumpDir, fileName);

        try {
            FileUtils.writeByteArrayToFile(dumpClassFile, data);
            dumpResult.put(clazz, dumpClassFile);
        } catch (IOException e) {
            logger.warn("dump class:{} to file {} failed.", className, dumpClassFile, e);
        }
    }

    private String buildDumpFileName(ClassLoader classLoader, String className) {
        String classFilePath = className.replace(".", File.separator) + ".class";
        if (classLoader != null) {
            return classLoader.getClass().getName() + "-" + Integer.toHexString(classLoader.hashCode()) +
                    File.separator + classFilePath;
        }
        return classFilePath;
    }
}
