public class arthas_0244 {
    private BufferedReader br;


        public static String simpleRequest(String url) {
            br = null;
            try {
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestProperty("Accept", "application/json");
                int responseCode = con.getResponseCode();
    
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }
                String result = sb.toString().trim();
                if (responseCode == 500) {
                    JSONObject errorObj = JSON.parseObject(result);
                    if (errorObj.containsKey("errorMsg")) {
                        return errorObj.getString("errorMsg");
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
