public class arthas_0115 {

        public static List<String> loadCommandHistoryString(File file, final boolean useCache) {
            boolean cacheEnabled = useCache;
            BufferedReader br = null;
            List<String> history = new ArrayList<>();
            try {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
                String line;
                while ((line = br.readLine()) != null) {
                    if (!StringUtils.isBlank(line)) {
                        history.add(line);
                    }
                }
            } catch (IOException e) {
                // ignore
            } finally {
                try {
                    if (br != null) {
                        br.close();
                    }
                } catch (IOException ioe) {
                    // ignore
                }
            }
            return history;
        }
}
