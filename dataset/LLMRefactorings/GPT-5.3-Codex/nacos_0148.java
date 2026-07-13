public class nacos_0148 {


        public static ParamCheckResponse checkGroupFormat(String group) {
            ParamCheckResponse paramCheckResponse = new ParamCheckResponse();
            if (StringUtils.isBlank(group)) {
                paramCheckResponse.setSuccess(true);
                return paramCheckResponse;
            }
            if (group.length() > paramCheckRule.maxGroupLength) {
                paramCheckResponse.setSuccess(false);
                paramCheckResponse.setMessage(
                    String.format("Param 'group' is illegal, the param length should not exceed %d.",
                        paramCheckRule.maxGroupLength));
                return paramCheckResponse;
            }
            if (!groupPattern.matcher(group).matches()) {
                paramCheckResponse.setSuccess(false);
                paramCheckResponse.setMessage(
                    "Param 'group' is illegal, illegal characters should not appear in the param.");
                return paramCheckResponse;
            }
            paramCheckResponse.setSuccess(true);
            return paramCheckResponse;
        
        }
}
