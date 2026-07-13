public class nacos_0181 {

        static File targetFile(String dataId, String group, String tenant) {
            try {
                ParamUtils.checkParam(dataId, group, tenant);
            } catch (Exception e) {
                throw new NacosRuntimeException(NacosException.CLIENT_INVALID_PARAM,
                    "parameter is invalid.");
            }
            // fix https://github.com/alibaba/nacos/issues/10067
            dataId = PathEncoderManager.getInstance().encode(dataId);
            group = PathEncoderManager.getInstance().encode(group);
            tenant = PathEncoderManager.getInstance().encode(tenant);
            File file = null;
            if (StringUtils.isBlank(tenant)) {
                file = new File(EnvUtil.getNacosHome(), BASE_DIR);
            } else {
                file = new File(EnvUtil.getNacosHome(), TENANT_BASE_DIR);
                file = new File(file, tenant);
            }
            file = new File(file, group);
            file = new File(file, dataId);
            return file;
        }
}
