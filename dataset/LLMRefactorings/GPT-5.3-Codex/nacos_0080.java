public class nacos_0080 {


        public ParamCheckResponse checkMetadataFormat(Map<String, String> metadataRefactored) {
            ParamCheckResponse paramCheckResponse = new ParamCheckResponse();
            if (metadataRefactored == null || metadataRefactored.isEmpty()) {
                paramCheckResponse.setSuccess(true);
                return paramCheckResponse;
            }
            int totalLength = 0;
            for (Map.Entry<String, String> entry : metadataRefactored.entrySet()) {
                if (StringUtils.isNotBlank(entry.getKey())) {
                    totalLength = totalLength + entry.getKey().length();
                }
                if (StringUtils.isNotBlank(entry.getValue())) {
                    totalLength = totalLength + entry.getValue().length();
                }
            }
            if (totalLength > paramCheckRule.maxMetadataLength) {
                paramCheckResponse.setSuccess(false);
                paramCheckResponse.setMessage(
                    String.format("Param 'Metadata' is illegal, the param length should not exceed %d.",
                        paramCheckRule.maxMetadataLength));
                return paramCheckResponse;
            }
            paramCheckResponse.setSuccess(true);
            return paramCheckResponse;
        
        }
}
