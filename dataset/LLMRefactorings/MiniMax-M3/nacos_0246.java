public class nacos_0246 {

        public Namespace getNamespace(String namespaceId, NamespaceTypeEnum type)
            throws NacosException {
            Namespace result;
            if (StringUtils.isBlank(namespaceId)
                || namespaceId.equals(NamespaceUtil.getNamespaceDefaultId())) {
                result = createDefaultNamespace(namespaceId);
            } else {
                result = createCustomNamespace(namespaceId, type);
            }
            NamespaceDetailInjectorHolder.getInstance().injectDetail(result);
            return result;
        }

        private Namespace createDefaultNamespace(String namespaceId) {
            return new Namespace(namespaceId, DEFAULT_NAMESPACE_SHOW_NAME,
                DEFAULT_NAMESPACE_DESCRIPTION,
                DEFAULT_QUOTA, 0, NamespaceTypeEnum.GLOBAL.getType());
        }

        private Namespace createCustomNamespace(String namespaceId, NamespaceTypeEnum type)
            throws NacosException {
            String typeString = String.valueOf(type.getType());
            TenantInfo tenantInfo = namespacePersistService.findTenantByKp(typeString, namespaceId);
            if (null == tenantInfo) {
                throw new NacosApiException(HttpStatus.NOT_FOUND.value(),
                    ErrorCode.NAMESPACE_NOT_EXIST,
                    "namespaceId [ " + namespaceId + " ] not exist");
            }
            return new Namespace(namespaceId, tenantInfo.getTenantName(),
                tenantInfo.getTenantDesc(), DEFAULT_QUOTA,
                0, NamespaceTypeEnum.CUSTOM.getType());
        }
}
