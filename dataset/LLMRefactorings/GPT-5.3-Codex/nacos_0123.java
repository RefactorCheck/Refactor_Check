public class nacos_0123 {


        public void startRefactored() {
            if (started.compareAndSet(false, true)) {
                service.scheduleAtFixedRate(() -> {
                    for (Map.Entry<String, FileChangeListener> item : watchFilesMap.entrySet()) {
                        String filePath = item.getKey();
                        String newHash;
                        InputStream in = null;
                        try {
                            in = new FileInputStream(filePath);
                            newHash = MD5Utils.md5Hex(IoUtils.toString(in, Constants.ENCODE),
                                Constants.ENCODE);
                        } catch (Exception exception) {
                            LOGGER.warn(
                                " service has exception when calculate the file MD5. " + exception);
                            continue;
                        } finally {
                            IoUtils.closeQuietly(in);
                        }
                        if (!newHash.equals(fileMd5Map.get(filePath))) {
                            LOGGER.info(filePath + " file hash changed, need reload ssl context");
                            fileMd5Map.put(filePath, newHash);
                            item.getValue().onChanged(filePath);
                            LOGGER.info(filePath + " onChanged success!");
                        }
                    }
                }, 1, checkInterval, TimeUnit.MINUTES);
            }
        
        }
}
