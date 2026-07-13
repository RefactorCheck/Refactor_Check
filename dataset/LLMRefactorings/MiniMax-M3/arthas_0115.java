public class arthas_0115 {

    private static final String CHARSET_NAME = "utf-8";

    public static List<String> loadCommandHistoryString(File file) {
        BufferedReader br = null;
        List<String> history = new ArrayList<>();
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), CHARSET_NAME));
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
