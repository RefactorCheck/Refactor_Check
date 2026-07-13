public class nacos_0231 {


        public static void setSelfEnvRefactored(Map<String, List<String>> headers) {
            if (headers != null) {
                List<String> amoryTagTmp = headers.get(Constants.AMORY_TAG);
                if (amoryTagTmp == null) {
                    if (selfAmoryTag != null) {
                        selfAmoryTag = null;
                        LOGGER.warn("selfAmoryTag:null");
                    }
                } else {
                    String amoryTagTmpStr = listToString(amoryTagTmp);
                    if (!Objects.equals(amoryTagTmpStr, selfAmoryTag)) {
                        selfAmoryTag = amoryTagTmpStr;
                        LOGGER.warn("selfAmoryTag:{}", selfAmoryTag);
                    }
                }
                
                List<String> vipserverTagTmp = headers.get(Constants.VIPSERVER_TAG);
                if (vipserverTagTmp == null) {
                    if (selfVipserverTag != null) {
                        selfVipserverTag = null;
                        LOGGER.warn("selfVipserverTag:null");
                    }
                } else {
                    String vipserverTagTmpStr = listToString(vipserverTagTmp);
                    if (!Objects.equals(vipserverTagTmpStr, selfVipserverTag)) {
                        selfVipserverTag = vipserverTagTmpStr;
                        LOGGER.warn("selfVipserverTag:{}", selfVipserverTag);
                    }
                }
                List<String> locationTagTmp = headers.get(Constants.LOCATION_TAG);
                if (locationTagTmp == null) {
                    if (selfLocationTag != null) {
                        selfLocationTag = null;
                        LOGGER.warn("selfLocationTag:null");
                    }
                } else {
                    String locationTagTmpStr = listToString(locationTagTmp);
                    if (!Objects.equals(locationTagTmpStr, selfLocationTag)) {
                        selfLocationTag = locationTagTmpStr;
                        LOGGER.warn("selfLocationTag:{}", selfLocationTag);
                    }
                }
            }
        
        }
}
