public class arthas_0072 {

        public static Response requestRefactored(String urlString) {
            HttpURLConnection urlConnection = null;
            InputStream in = null;
            try {
                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
                urlConnection.setReadTimeout(READ_TIMEOUT);;
                // prefer json to text
                urlConnection.setRequestProperty("Accept", "application/json,text/plain;q=0.2");
                in = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line = null;
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                int statusCode = urlConnection.getResponseCode();
                String result = sb.toString().trim();
                if (statusCode == INTERNAL_SERVER_ERROR) {
                    JSONObject errorObj = JSON.parseObject(result);
                    if (errorObj.containsKey("errorMsg")) {
                        return new Response(errorObj.getString("errorMsg"), false);
                    }
                    return new Response(result, false);
                }
                return new Response(result);
            } catch (IOException e) {
                return new Response(e.getMessage(), false);
            } finally {
                IOUtils.close(in);
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }
}
