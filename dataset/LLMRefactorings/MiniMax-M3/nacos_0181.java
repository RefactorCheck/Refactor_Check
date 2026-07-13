public class nacos_0181 {

    static File targetFile(String dataId, String group, String tenant) {
        try {
            ParamUtils.checkParam(dataId, group, tenant);
        } catch (Exception e) {
            throw new NacosRuntimeException(NacosException.CLIENT_INVALID_PARAM,
                "parameter is invalid.");
        }
        dataId = PathEncoderManager.getInstance().encode(dataId);
        group = PathEncoderManager.getInstance().encode(group);
        tenant = PathEncoderManager.getInstance().encode(tenant);
        File file = buildBaseDir(tenant);
        file = new File(file, group);
        file = new File(file, dataId);
        return file;
    }

    private static File buildBaseDir(String tenant) {
        if (StringUtils.isBlank(tenant)) {
            return new File(EnvUtil.getNacosHome(), BASE_DIR);
        }
        File file = new File(EnvUtil.getNacosHome(), TENANT_BASE_DIR);
        return new File(file, tenant);
    }
}
