public class nacos_0148 {

        private ParamCheckResponse createFailureResponse(String message) {
            ParamCheckResponse paramCheckResponse = new ParamCheckResponse();
            paramCheckResponse.setSuccess(false);
            paramCheckResponse.setMessage(message);
            return paramCheckResponse;
        }

        public ParamCheckResponse checkGroupFormat(String group) {
            if (StringUtils.isBlank(group)) {
                ParamCheckResponse paramCheckResponse = new ParamCheckResponse();
                paramCheckResponse.setSuccess(true);
                return paramCheckResponse;
            }
            if (group.length() > paramCheckRule.maxGroupLength) {
                return createFailureResponse(
                    String.format("Param 'group' is illegal, the param length should not exceed %d.",
                        paramCheckRule.maxGroupLength));
            }
            if (!groupPattern.matcher(group).matches()) {
                return createFailureResponse(
                    "Param 'group' is illegal, illegal characters should not appear in the param.");
            }
            ParamCheckResponse paramCheckResponse = new ParamCheckResponse();
            paramCheckResponse.setSuccess(true);
            return paramCheckResponse;
        }
}
