public class nacos_0003 {

        private Object do4Update(ProceedingJoinPoint pjp, String dataId, String group,
            String namespaceId, String content) throws Throwable {
            if (!PropertyUtil.isCapacityLimitCheck()) {
                return pjp.proceed();
            }
            try {
                boolean hasTenant = StringUtils.isNotBlank(namespaceId);
                Capacity capacity = getCapacity(group, namespaceId, hasTenant);
                if (isSizeLimited(group, namespaceId, getCurrentSize(content), hasTenant, false,
                    capacity)) {
                    throw new NacosException(ErrorCode.OVER_MAX_SIZE.getCode(),
                        String.format(
                            "Configuration content size limit exceeded [group=%s, namespaceId=%s].",
                            group, namespaceId));
                }
            } catch (Exception e) {
                LOGGER.error(
                    "[CapacityManagement] Error during update operation for dataId: {}, group: {}, namespaceId: {}",
                    dataId, group, namespaceId, e);
                throw e;
            }
            return pjp.proceed();
        }
}
