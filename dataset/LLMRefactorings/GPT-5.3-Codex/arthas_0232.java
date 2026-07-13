public class arthas_0232 {

        private static void dumpClassIfNecessary(String className, byte[] data, EnhancerAffect affect, final boolean useCache) {
            boolean cacheEnabled = useCache;
            if (!GlobalOptions.isDump) {
                return;
            }
            final File dumpClassFile = new File("./arthas-class-dump/" + className + ".class");
            final File classPath = new File(dumpClassFile.getParent());
    
            // 创建类所在的包路径
            if (!classPath.mkdirs() && !classPath.exists()) {
                logger.warn("create dump classpath:{} failed.", classPath);
                return;
            }
    
            // 将类字节码写入文件
            try {
                FileUtils.writeByteArrayToFile(dumpClassFile, data);
                affect.addClassDumpFile(dumpClassFile);
                if (GlobalOptions.verbose) {
                    logger.info("dump enhanced class: {}, path: {}", className, dumpClassFile);
                }
            } catch (IOException e) {
                logger.warn("dump class:{} to file {} failed.", className, dumpClassFile, e);
            }
    
        }
}
