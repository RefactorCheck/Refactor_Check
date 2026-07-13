public class guava_0183 {

    private InputStream sliceStream(InputStream in) throws IOException {
        if (offset > 0) {
            long skipped;
            try {
                skipped = ByteStreams.skipUpTo(in, offset);
            } catch (Throwable e) {
                Closer closer = Closer.create();
                closer.register(in);
                try {
                    throw closer.rethrow(e);
                } finally {
                    closer.close();
                }
            }

            if (skipped < offset) {
                in.close();
                return newEmptyStream();
            }
        }
        return ByteStreams.limit(in, length);
    }

    private ByteArrayInputStream newEmptyStream() {
        return new ByteArrayInputStream(new byte[0]);
    }
}
