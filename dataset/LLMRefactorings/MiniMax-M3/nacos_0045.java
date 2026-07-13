public class nacos_0045 {

        private static File targetGrayFile(String dataId, String group, String tenant,
            String grayName) {
            try {
                ParamUtils.checkParam(grayName);
                ParamUtils.checkParam(dataId, group, tenant);
            } catch (Exception e) {
                throw new NacosRuntimeException(NacosException.CLIENT_INVALID_PARAM,
                    "parameter is invalid.");
            }
            // fix https://github.com/alibaba/nacos/issues/10067
            dataId = PathEncoderManager.getInstance().encode(dataId);
            group = PathEncoderManager.getInstance().encode(group);
            tenant = PathEncoderManager.getInstance().encode(tenant);
            
            return buildGrayFilePath(tenant, group, dataId, grayName);
        }

        private static File buildGrayFilePath(String tenant, String group, String dataId, String grayName) {
            File file;
            if (StringUtils.isBlank(tenant)) {
                file = new File(EnvUtil.getNacosHome(), GRAY_DIR);
            } else {
                file = new File(EnvUtil.getNacosHome(), TENANT_GRAY_DIR);
                file = new File(file, tenant);
            }
            file = new File(file, group);
            file = new File(file, dataId);
            file = new File(file, grayName);
            return file;
        }
}
