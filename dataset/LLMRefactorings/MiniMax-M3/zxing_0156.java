public class zxing_0156 {

      private static final int MIN_VERSION_FOR_INFO = 7;
      private static final int VERSION_INFO_SIZE = 6;
      private static final int VERSION_INFO_DIMENSION = 3;
      private static final int VERSION_INFO_OFFSET = 11;

      static void maybeEmbedVersionInfo(Version version, ByteMatrix matrix) throws WriterException {
        if (version.getVersionNumber() < MIN_VERSION_FOR_INFO) {
          return;
        }
        BitArray versionInfoBits = new BitArray();
        makeVersionInfoBits(version, versionInfoBits);

        int bitIndex = VERSION_INFO_SIZE * VERSION_INFO_DIMENSION - 1;
        for (int i = 0; i < VERSION_INFO_SIZE; ++i) {
          for (int j = 0; j < VERSION_INFO_DIMENSION; ++j) {
            boolean bit = versionInfoBits.get(bitIndex);
            bitIndex--;
            matrix.set(i, matrix.getHeight() - VERSION_INFO_OFFSET + j, bit);
            matrix.set(matrix.getHeight() - VERSION_INFO_OFFSET + j, i, bit);
          }
        }
      }
}
