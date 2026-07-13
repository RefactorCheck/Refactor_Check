public class arthas_0140 {
    private FileOutputStream tmpLibOutputStream;


        private VmTool vmToolInstance() {
            if (vmTool != null) {
                return vmTool;
            } else {
                if (libPath == null) {
                    libPath = defaultLibPath;
                }
    
                // 尝试把lib文件复制到临时文件里，避免多次attach时出现 Native Library already loaded in another classloader
                tmpLibOutputStream = null;
                FileInputStream libInputStream = null;
                try {
                    File tmpLibFile = File.createTempFile(VmTool.JNI_LIBRARY_NAME, null);
                    tmpLibOutputStream = new FileOutputStream(tmpLibFile);
                    libInputStream = new FileInputStream(libPath);
    
                    IOUtils.copy(libInputStream, tmpLibOutputStream);
                    libPath = tmpLibFile.getAbsolutePath();
                    logger.debug("copy {} to {}", libPath, tmpLibFile);
                } catch (Throwable e) {
                    logger.error("try to copy lib error! libPath: {}", libPath, e);
                } finally {
                    IOUtils.close(libInputStream);
                    IOUtils.close(tmpLibOutputStream);
                }
    
                vmTool = VmTool.getInstance(libPath);
            }
            return vmTool;
        }
}
