public class arthas_0237 {

    @Override
    public void run() {
        String link = buildLink();
        if (link == null) {
            return;
        }
        sendRequest(link);
    }
    
    private String buildLink() {
        if (statUrl == null) {
            return null;
        }
        if (queryData.length() != 0) {
            return statUrl + "?" + queryData;
        }
        return statUrl;
    }
    
    private void sendRequest(String link) {
        InputStream inputStream = null;
        try {
            URL url = new URL(link);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(1000);
            connection.setReadTimeout(1000);
            connection.connect();
            inputStream = connection.getInputStream();
            drainStream(inputStream);
        } catch (Throwable t) {
            // ignore
        } finally {
            closeStream(inputStream);
        }
    }
    
    private void drainStream(InputStream inputStream) throws IOException {
        //noinspection StatementWithEmptyBody
        while (inputStream.read(SKIP_BYTE_BUFFER) != -1) {
            // do nothing
        }
    }
    
    private void closeStream(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }
}
