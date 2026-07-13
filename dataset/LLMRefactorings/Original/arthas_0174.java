public class arthas_0174 {

        public static String toString(InputStream input) throws IOException {
            BufferedReader br = null;
            try {
                StringBuilder sb = new StringBuilder();
                br = new BufferedReader(new InputStreamReader(input));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                return sb.toString();
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
