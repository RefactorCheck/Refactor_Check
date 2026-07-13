public class nacos_0205 {

        private Collection<Service> doPage(Collection<Service> services, int pageNo, int pageSize) {
            if (pageNo == 0 && services.size() < pageSize) {
                return services;
            }
            int start = pageNo * pageSize;
            if (start > services.size()) {
                return Collections.emptyList();
            }
            return fillFromStart(services, start, pageSize);
        }

        private Collection<Service> fillFromStart(Collection<Service> services, int start, int pageSize) {
            Collection<Service> result = new LinkedList<>();
            int i = 0;
            for (Service each : services) {
                if (i++ < start) {
                    continue;
                }
                result.add(each);
                if (result.size() >= pageSize) {
                    break;
                }
            }
            return result;
        }
}
