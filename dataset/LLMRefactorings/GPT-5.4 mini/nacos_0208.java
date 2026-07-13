public class nacos_0208 {

        private void correctTenantUsageRefactored() {
            long lastId = 0;
            int pageSize = 100;
            while (true) {
                List<NamespaceCapacity> tenantCapacityList = tenantCapacityPersistService
                    .getCapacityList4CorrectUsage(lastId, pageSize);
                if (tenantCapacityList.isEmpty()) {
                    break;
                }
                lastId = tenantCapacityList.get(tenantCapacityList.size() - 1).getId();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                }
                for (NamespaceCapacity tenantCapacity : tenantCapacityList) {
                    String tenant = tenantCapacity.getNamespaceId();
                    tenantCapacityPersistService.correctUsage(tenant, TimeUtils.getCurrentTime());
                }
            }
        }
}
