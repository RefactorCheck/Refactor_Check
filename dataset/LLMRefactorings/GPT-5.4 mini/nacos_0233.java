public class nacos_0233 {

        private LimitType getGroupOrTenantLimitTypeRefactored(CounterMode counterMode, String group,
            String namespaceId, int currentSize,
            boolean hasTenant) {
            if (group == null) {
                return null;
            }
            Capacity capacity = getCapacity(group, namespaceId, hasTenant);
            if (isSizeLimited(group, namespaceId, currentSize, hasTenant, false, capacity)) {
                return LimitType.OVER_MAX_SIZE;
            }
            if (capacity == null) {
                insertCapacity(group, namespaceId, hasTenant);
            }
            boolean updateSuccess = isUpdateSuccess(counterMode, group, namespaceId, hasTenant);
            if (updateSuccess) {
                return null;
            }
            if (hasTenant) {
                return LimitType.OVER_TENANT_QUOTA;
            }
            return LimitType.OVER_GROUP_QUOTA;
        }
}
