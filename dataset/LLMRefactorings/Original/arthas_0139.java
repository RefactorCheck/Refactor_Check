public class arthas_0139 {

        private void dumpClassIfNecessary(Class<?> clazz, byte[] data) {
            String className = clazz.getName();
            ClassLoader classLoader = clazz.getClassLoader();
    
            // 创建类所在的包路径
            File dumpDir = dumpDir();
            if (!dumpDir.mkdirs() && !dumpDir.exists()) {
                logger.warn("create dump directory:{} failed.", dumpDir.getAbsolutePath());
                return;
            }
    
            String fileName;
            if (classLoader != null) {
                fileName = classLoader.getClass().getName() + "-" + Integer.toHexString(classLoader.hashCode()) +
                        File.separator + className.replace(".", File.separator) + ".class";
            } else {
                fileName = className.replace(".", File.separator) + ".class";
            }
    
            File dumpClassFile = new File(dumpDir, fileName);
    
            // 将类字节码写入文件
            try {
                FileUtils.writeByteArrayToFile(dumpClassFile, data);
                dumpResult.put(clazz, dumpClassFile);
            } catch (IOException e) {
                logger.warn("dump class:{} to file {} failed.", className, dumpClassFile, e);
            }
        }
}
