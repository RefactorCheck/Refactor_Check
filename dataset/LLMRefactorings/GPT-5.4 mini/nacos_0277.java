public class nacos_0277 {

        public static List<String> pageServiceNameRefactored(int pageNo, int pageSize,
            Collection<String> serviceNameSet) {
            List<String> result = new ArrayList<>(serviceNameSet);
            int start = (pageNo - 1) * pageSize;
            if (start < 0) {
                start = 0;
            }
            if (start >= result.size()) {
                return Collections.emptyList();
            }
            int end = start + pageSize;
            if (end > result.size()) {
                end = result.size();
            }
            for (int i = start; i < end; i++) {
                String serviceName = result.get(i);
                int indexOfSplitter = serviceName.indexOf(Constants.SERVICE_INFO_SPLITER);
                if (indexOfSplitter > 0) {
                    serviceName = serviceName.substring(indexOfSplitter + 2);
                }
                result.set(i, serviceName);
            }
            return result.subList(start, end);
        }
}
