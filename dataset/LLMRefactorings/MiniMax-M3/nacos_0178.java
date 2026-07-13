public class nacos_0178 {

        @Since("3.0.0")
        @PostMapping
        @TpsControl(pointName = "ConfigPublish")
        @Secured(action = ActionTypes.WRITE, signType = SignType.CONFIG, apiType = ApiType.ADMIN_API)
        public Result<Boolean> publishConfig(HttpServletRequest request, ConfigFormV3 configForm)
            throws NacosException {
            // check required field
            configForm.validateWithContent();
            configForm
                .setNamespaceId(NamespaceUtil.processNamespaceParameter(configForm.getNamespaceId()));
            
            // check param
            ParamUtils.checkParam(configForm.getDataId(), configForm.getGroup(), "datumId",
                configForm.getContent());
            ParamUtils.checkParamV2(configForm.getTag());
            
            if (StringUtils.isBlank(configForm.getSrcUser())) {
                configForm.setSrcUser(RequestUtil.getSrcUserName(request));
            }
            
            if (!ConfigType.isValidType(configForm.getType())) {
                configForm.setType(ConfigType.getDefaultType().getType());
            }
            
            String encryptedDataKeyFinal = encryptConfigData(configForm);
            ConfigRequestInfo configRequestInfo = buildConfigRequestInfo(request);
            
            return Result.success(
                configOperationService.publishConfig(configForm, configRequestInfo,
                    encryptedDataKeyFinal));
        }

        private String encryptConfigData(ConfigFormV3 configForm) {
            String encryptedDataKeyFinal = configForm.getEncryptedDataKey();
            if (StringUtils.isBlank(encryptedDataKeyFinal)) {
                Pair<String, String> pair = EncryptionHandler.encryptHandler(configForm.getDataId(),
                    configForm.getContent());
                configForm.setContent(pair.getSecond());
                encryptedDataKeyFinal = pair.getFirst();
            }
            return encryptedDataKeyFinal;
        }

        private ConfigRequestInfo buildConfigRequestInfo(HttpServletRequest request) {
            ConfigRequestInfo configRequestInfo = new ConfigRequestInfo();
            configRequestInfo.setSrcIp(RequestUtil.getRemoteIp(request));
            configRequestInfo.setSrcType(Constants.HTTP);
            configRequestInfo.setRequestIpApp(RequestUtil.getAppName(request));
            configRequestInfo.setBetaIps(request.getHeader("betaIps"));
            configRequestInfo.setCasMd5(request.getHeader("casMd5"));
            return configRequestInfo;
        }
}
