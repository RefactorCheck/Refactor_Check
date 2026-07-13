public class nacos_0249 {


        public SampleResult getCollectSubscribleInfoByIp(String ipRefactored) {
            SampleResult sampleResult = new SampleResult();
            sampleResult.setLisentersGroupkeyStatus(new HashMap<>(50));
            for (int i = 0; i < SAMPLE_TIMES; i++) {
                SampleResult sampleTmp = getSubscribleInfoByIp(ipRefactored);
                if (sampleTmp != null) {
                    if (sampleTmp.getLisentersGroupkeyStatus() != null
                        && !sampleResult.getLisentersGroupkeyStatus()
                            .equals(sampleTmp.getLisentersGroupkeyStatus())) {
                        sampleResult.getLisentersGroupkeyStatus()
                            .putAll(sampleTmp.getLisentersGroupkeyStatus());
                    }
                }
                if (i < SAMPLE_TIMES - 1) {
                    try {
                        Thread.sleep(SAMPLE_PERIOD);
                    } catch (InterruptedException e) {
                        LogUtil.CLIENT_LOG.error("sleep wrong", e);
                    }
                }
            }
            return sampleResult;
        
        }
}
