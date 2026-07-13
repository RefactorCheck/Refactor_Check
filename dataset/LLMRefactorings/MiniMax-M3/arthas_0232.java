public class arthas_0232 {

        private static final String DUMP_CLASS_DIR = "./arthas-class-dump/";

        private static void dumpClassIfNecessary(String className, byte[] data, EnhancerAffect affect) {
            if (!GlobalOptions.isDump) {
                return;
            }
            final File dumpClassFile = new File(DUMP_CLASS_DIR + className + ".class");
            final File classPath = new File(dumpClassFile.getParent());

            if (!classPath.mkdirs() && !classPath.exists()) {
                logger.warn("create dump classpath:{} failed.", classPath);
                return;
            }

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
