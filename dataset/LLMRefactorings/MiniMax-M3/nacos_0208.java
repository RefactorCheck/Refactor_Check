public class nacos_0208 {

    private static final int PAGE_SIZE = 100;
    private static final long SLEEP_DURATION_MS = 100;

    private void correctTenantUsage() {
        long lastId = 0;
        while (true) {
            List<NamespaceCapacity> tenantCapacityList = tenantCapacityPersistService
                .getCapacityList4CorrectUsage(lastId, PAGE_SIZE);
            if (tenantCapacityList.isEmpty()) {
                break;
            }
            lastId = tenantCapacityList.get(tenantCapacityList.size() - 1).getId();
            try {
                Thread.sleep(SLEEP_DURATION_MS);
            } catch (InterruptedException ignored) {
            }
            for (NamespaceCapacity tenantCapacity : tenantCapacityList) {
                String tenant = tenantCapacity.getNamespaceId();
                tenantCapacityPersistService.correctUsage(tenant, TimeUtils.getCurrentTime());
            }
        }
    }
}
