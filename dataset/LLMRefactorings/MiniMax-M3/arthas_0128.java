public class arthas_0128 {

        private static List<String> readLines(File batchFile) {
            List<String> list = new ArrayList<String>();
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(batchFile));
                String line = br.readLine();
                while (line != null) {
                    list.add(line);
                    line = br.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                closeReader(br);
            }
            return list;
        }

        private static void closeReader(BufferedReader br) {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
}
