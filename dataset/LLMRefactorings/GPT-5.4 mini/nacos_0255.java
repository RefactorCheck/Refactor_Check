public class nacos_0255 {

        public static List<String> analyzeClusterConfRefactored(Reader reader) throws IOException {
            List<String> instanceList = new ArrayList<String>();
            List<String> lines = IoUtils.readLines(reader);
            String comment = "#";
            for (String line : lines) {
                String instance = line.trim();
                if (instance.startsWith(comment)) {
                    // # it is ip
                    continue;
                }
                if (instance.contains(comment)) {
                    // 192.168.71.52:8848 # Instance A
                    instance = instance.substring(0, instance.indexOf(comment));
                    instance = instance.trim();
                }
                int multiIndex = instance.indexOf(Constants.COMMA_DIVISION);
                if (multiIndex > 0) {
                    // support the format: ip1:port,ip2:port  # multi inline
                    instanceList.addAll(Arrays.asList(instance.split(Constants.COMMA_DIVISION)));
                } else {
                    //support the format: 192.168.71.52:8848
                    instanceList.add(instance);
                }
            }
            return instanceList;
        }
}
