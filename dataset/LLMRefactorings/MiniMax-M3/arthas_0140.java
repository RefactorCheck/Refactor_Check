public class arthas_0140 {

        private VmTool vmToolInstance() {
            if (vmTool != null) {
                return vmTool;
            } else {
                if (libPath == null) {
                    libPath = defaultLibPath;
                }

                copyLibToTempFile();

                vmTool = VmTool.getInstance(libPath);
            }
            return vmTool;
        }

        private void copyLibToTempFile() {
            FileOutputStream tmpLibOutputStream = null;
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
        }
}
