public class arthas_0106 {

        private static void saveUrl(final String filename, final String urlString, boolean printProgress)
                throws IOException {
            BufferedInputStream in = null;
            FileOutputStream fout = null;
            try {
                URLConnection connection = openURLConnection(urlString);
                in = new BufferedInputStream(connection.getInputStream());
                List<String> values = connection.getHeaderFields().get("Content-Length");
                int fileSize = 0;
                if (values != null && !values.isEmpty()) {
                    String contentLength = values.get(0);
                    if (contentLength != null) {
                        // parse the length into an integer...
                        fileSize = Integer.parseInt(contentLength);
                    }
                }
    
                fout = new FileOutputStream(filename);
    
                final byte[] data = new byte[1024 * 1024];
                int totalCount = 0;
                int count;
                long lastPrintTime = System.currentTimeMillis();
                while ((count = in.read(data, 0, data.length)) != -1) {
                    totalCount += count;
                    if (printProgress) {
                        long now = System.currentTimeMillis();
                        if (now - lastPrintTime > 1000) {
                            AnsiLog.info("File size: {}, downloaded size: {}, downloading ...", formatFileSize(fileSize),
                                    formatFileSize(totalCount));
                            lastPrintTime = now;
                        }
                    }
                    fout.write(data, 0, count);
                }
            } catch (javax.net.ssl.SSLException e) {
                AnsiLog.error("TLS connect error, please check local Java TLS configuration.");
                AnsiLog.error("URL: " + urlString);
                AnsiLog.error(e);
            } finally {
                IOUtils.close(in);
                IOUtils.close(fout);
            }
        }
}
