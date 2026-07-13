public class nacos_0255 {

        public static List<String> analyzeClusterConf(Reader reader) throws IOException {
            List<String> instanceList = new ArrayList<String>();
            List<String> lines = IoUtils.readLines(reader);
            for (String line : lines) {
                String instance = processInstance(line);
                if (instance == null) {
                    continue;
                }
                int multiIndex = instance.indexOf(Constants.COMMA_DIVISION);
                if (multiIndex > 0) {
                    instanceList.addAll(Arrays.asList(instance.split(Constants.COMMA_DIVISION)));
                } else {
                    instanceList.add(instance);
                }
            }
            return instanceList;
        }

        private static String processInstance(String line) {
            String comment = "#";
            String instance = line.trim();
            if (instance.startsWith(comment)) {
                return null;
            }
            if (instance.contains(comment)) {
                instance = instance.substring(0, instance.indexOf(comment));
                instance = instance.trim();
            }
            return instance;
        }
}
