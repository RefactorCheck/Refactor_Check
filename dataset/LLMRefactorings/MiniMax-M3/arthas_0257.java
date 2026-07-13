public class arthas_0257 {

        private AsyncProfiler profilerInstance() {
            if (profiler != null) {
                return profiler;
            }
    
            // try to load from special path
            if (ProfilerAction.load.toString().equals(action)) {
                profiler = AsyncProfiler.getInstance(this.actionArg);
            }
    
            if (libPath != null) {
                libPath = copyLibToTempFile(libPath);
                profiler = AsyncProfiler.getInstance(libPath);
            } else {
                if (OSUtils.isLinux() || OSUtils.isMac()) {
                    throw new IllegalStateException("Can not find libasyncProfiler so, please check the arthas directory.");
                } else {
                    throw new IllegalStateException("Current OS do not support AsyncProfiler, Only support Linux/Mac.");
                }
            }
    
            return profiler;
        }

        private String copyLibToTempFile(String libPath) {
            FileOutputStream tmpLibOutputStream = null;
            FileInputStream libInputStream = null;
            try {
                File tmpLibFile = File.createTempFile(VmTool.JNI_LIBRARY_NAME, null);
                tmpLibOutputStream = new FileOutputStream(tmpLibFile);
                libInputStream = new FileInputStream(libPath);

                IOUtils.copy(libInputStream, tmpLibOutputStream);
                logger.debug("copy {} to {}", libPath, tmpLibFile);
                return tmpLibFile.getAbsolutePath();
            } catch (Throwable e) {
                logger.error("try to copy lib error! libPath: {}", libPath, e);
                return libPath;
            } finally {
                IOUtils.close(libInputStream);
                IOUtils.close(tmpLibOutputStream);
            }
        }
}
