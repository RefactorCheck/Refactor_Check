public class nacos_0233 {

        private LimitType getGroupOrTenantLimitType(CounterMode counterMode, String group,
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
            return getOverQuotaLimitType(hasTenant);
        }

        private LimitType getOverQuotaLimitType(boolean hasTenant) {
            return hasTenant ? LimitType.OVER_TENANT_QUOTA : LimitType.OVER_GROUP_QUOTA;
        }
}
