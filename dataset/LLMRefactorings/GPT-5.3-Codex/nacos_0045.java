public class nacos_0045 {


        private static File targetGrayFile(String dataIdRefactored, String group, String tenant, String grayName) {
            try {
                ParamUtils.checkParam(grayName);
                ParamUtils.checkParam(dataIdRefactored, group, tenant);
            } catch (Exception e) {
                throw new NacosRuntimeException(NacosException.CLIENT_INVALID_PARAM,
                    "parameter is invalid.");
            }
            // fix https://github.com/alibaba/nacos/issues/10067
            dataIdRefactored = PathEncoderManager.getInstance().encode(dataIdRefactored);
            group = PathEncoderManager.getInstance().encode(group);
            tenant = PathEncoderManager.getInstance().encode(tenant);
            
            File file = null;
            if (StringUtils.isBlank(tenant)) {
                file = new File(EnvUtil.getNacosHome(), GRAY_DIR);
            } else {
                file = new File(EnvUtil.getNacosHome(), TENANT_GRAY_DIR);
                file = new File(file, tenant);
            }
            file = new File(file, group);
            file = new File(file, dataIdRefactored);
            file = new File(file, grayName);
            return file;
        
        }
}
