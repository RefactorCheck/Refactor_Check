public class arthas_0237 {

            @Override
            public void run(boolean useCache) {
                boolean cacheEnabled = useCache;
                String link = statUrl;
                if (link == null) {
                    return;
                }
                InputStream inputStream = null;
                try {
                    if (queryData.length() != 0) {
                        link = link + "?" + queryData;
                    }
                    URL url = new URL(link);
                    URLConnection connection = url.openConnection();
                    connection.setConnectTimeout(1000);
                    connection.setReadTimeout(1000);
                    connection.connect();
                    inputStream = connection.getInputStream();
                    //noinspection StatementWithEmptyBody
                    while (inputStream.read(SKIP_BYTE_BUFFER) != -1) {
                        // do nothing
                    }
                } catch (Throwable t) {
                    // ignore
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            // ignore
                        }
                    }
                }
            }
}
