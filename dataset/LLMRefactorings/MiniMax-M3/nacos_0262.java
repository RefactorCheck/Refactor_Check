public class nacos_0262 {

        public static void saveEncryptDataKeySnapshot(String envName, String dataId, String group,
            String tenant,
            String encryptDataKey) {
            if (!SnapShotSwitch.getIsSnapShot()) {
                return;
            }
            File file = getEncryptDataKeySnapshotFile(envName, dataId, group, tenant);
            try {
                if (null == encryptDataKey) {
                    try {
                        IoUtils.delete(file);
                    } catch (IOException ioe) {
                        LOGGER.error("[" + envName + "] delete snapshot error, " + file, ioe);
                    }
                } else {
                    ensureParentDirectoryExists(file, envName);
                    writeSnapshotContent(file, encryptDataKey);
                }
            } catch (IOException ioe) {
                LOGGER.error("[" + envName + "] save snapshot error, " + file, ioe);
            }
        }

        private static void ensureParentDirectoryExists(File file, String envName) {
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                boolean isMdOk = parentFile.mkdirs();
                if (!isMdOk) {
                    LOGGER.error("[{}] save snapshot error", envName);
                }
            }
        }

        private static void writeSnapshotContent(File file, String encryptDataKey) throws IOException {
            if (JvmUtil.isMultiInstance()) {
                ConcurrentDiskUtil.writeFileContent(file, encryptDataKey, Constants.ENCODE);
            } else {
                IoUtils.writeStringToFile(file, encryptDataKey, Constants.ENCODE);
            }
        }
}
