public class nacos_0277 {

    public static List<String> pageServiceName(int pageNo, int pageSize,
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
            result.set(i, stripServiceNamePrefix(result.get(i)));
        }
        return result.subList(start, end);
    }

    private static String stripServiceNamePrefix(String serviceName) {
        int indexOfSplitter = serviceName.indexOf(Constants.SERVICE_INFO_SPLITER);
        if (indexOfSplitter > 0) {
            return serviceName.substring(indexOfSplitter + 2);
        }
        return serviceName;
    }
}
