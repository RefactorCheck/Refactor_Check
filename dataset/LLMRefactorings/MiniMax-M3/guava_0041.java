public class guava_0041 {

        private InputStream sliceStream(InputStream in) throws IOException {
          if (offset > 0) {
            long skipped;
            try {
              skipped = ByteStreams.skipUpTo(in, offset);
            } catch (Throwable e) {
              handleSkipException(in, e);
              return new ByteArrayInputStream(new byte[0]);
            }

            if (skipped < offset) {
              in.close();
              return new ByteArrayInputStream(new byte[0]);
            }
          }
          return ByteStreams.limit(in, length);
        }

        private void handleSkipException(InputStream in, Throwable e) throws IOException {
          Closer closer = Closer.create();
          closer.register(in);
          try {
            throw closer.rethrow(e);
          } finally {
            closer.close();
          }
        }
}
