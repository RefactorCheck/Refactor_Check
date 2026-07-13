public class arthas_0244 {

    private static final String HEADER_ACCEPT = "Accept";
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String NEW_LINE = "\n";
    private static final String KEY_ERROR_MSG = "errorMsg";

    public static String simpleRequest(String url) {
        BufferedReader br = null;
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestProperty(HEADER_ACCEPT, CONTENT_TYPE_JSON);
            int responseCode = con.getResponseCode();

            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append(NEW_LINE);
            }
            String result = sb.toString().trim();
            if (responseCode == 500) {
                JSONObject errorObj = JSON.parseObject(result);
                if (errorObj.containsKey(KEY_ERROR_MSG)) {
                    return errorObj.getString(KEY_ERROR_MSG);
                }
                return result;
            } else {
                return result;
            }

        } catch (Exception e) {
            return null;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }
}
