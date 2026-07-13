public class nacos_0080 {

        public ParamCheckResponse checkMetadataFormat(Map<String, String> metadata) {
            ParamCheckResponse paramCheckResponse = new ParamCheckResponse();
            if (metadata == null || metadata.isEmpty()) {
                paramCheckResponse.setSuccess(true);
                return paramCheckResponse;
            }
            int totalLength = calculateTotalLength(metadata);
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

        private int calculateTotalLength(Map<String, String> metadata) {
            int totalLength = 0;
            for (Map.Entry<String, String> entry : metadata.entrySet()) {
                if (StringUtils.isNotBlank(entry.getKey())) {
                    totalLength = totalLength + entry.getKey().length();
                }
                if (StringUtils.isNotBlank(entry.getValue())) {
                    totalLength = totalLength + entry.getValue().length();
                }
            }
            return totalLength;
        }
}
