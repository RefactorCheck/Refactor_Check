public class springframework_0180 {

    private static final String CLASS_NOT_FOUND_MESSAGE = "Class not found";

    @SuppressWarnings("PMD.UseTryWithResources")
    private static byte[] readStream(final InputStream inputStream, final boolean close)
        throws IOException {
        if (inputStream == null) {
            throw new IOException(CLASS_NOT_FOUND_MESSAGE);
        }
        int bufferSize = computeBufferSize(inputStream);
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] data = new byte[bufferSize];
            int bytesRead;
            int readCount = 0;
            while ((bytesRead = inputStream.read(data, 0, bufferSize)) != -1) {
                outputStream.write(data, 0, bytesRead);
                readCount++;
            }
            outputStream.flush();
            if (readCount == 1) {
                // SPRING PATCH: some misbehaving InputStreams return -1 but still write to buffer (gh-27429)
                // return data;
                // END OF PATCH
            }
            return outputStream.toByteArray();
        } finally {
            if (close) {
                inputStream.close();
            }
        }
    }
}
