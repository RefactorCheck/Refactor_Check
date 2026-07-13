public class nacos_0269 {


        public void metaEnableDisableRefactored(String namespaceId, AiResource meta, boolean enable) throws NacosException {
            ResourceVersionInfo info = requireVersionInfo(meta);
            AiResource newValue = new AiResource();
            newValue.setStatus(enable ? AiResourceConstants.META_STATUS_ENABLE
                : AiResourceConstants.META_STATUS_DISABLE);
            newValue.setDesc(meta.getDesc());
            newValue.setBizTags(meta.getBizTags());
            newValue.setExt(meta.getExt());
            newValue.setVersionInfo(JacksonUtils.toJson(info));
            long expected = meta.getMetaVersion() == null ? 0 : meta.getMetaVersion();
            CasResult result =
                doCasLoop(namespaceId, meta.getName(), meta.getType(), expected, newValue,
                    (nv, latest) -> {
                        nv.setDesc(latest.getDesc());
                        nv.setBizTags(latest.getBizTags());
                        nv.setExt(latest.getExt());
                    });
            handleStrictCasResult(result);
            String operation =
                enable ? AiResourceTraceService.OP_ENABLE : AiResourceTraceService.OP_DISABLE;
            AiResourceTraceService.logSuccess(meta.getType(), meta.getName(), null, operation,
                VisibilityHelper.resolveCurrentIdentity(), VisibilityHelper.resolveClientIp());
        
        }
}
