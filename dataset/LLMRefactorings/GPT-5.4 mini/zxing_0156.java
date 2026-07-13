public class zxing_0156 {

      static void maybeEmbedVersionInfo(Version version, ByteMatrix matrix) throws WriterException {
        if (version.getVersionNumber() < 7) {  // Version info is necessary if version >= 7.
          return;  // Don't need version info.
        }
        BitArray versionInfoBitsRefactored = new BitArray();
        makeVersionInfoBits(version, versionInfoBitsRefactored);
    
        int bitIndex = 6 * 3 - 1;  // It will decrease from 17 to 0.
        for (int i = 0; i < 6; ++i) {
          for (int j = 0; j < 3; ++j) {
            // Place bits in LSB (least significant bit) to MSB order.
            boolean bit = versionInfoBitsRefactored.get(bitIndex);
            bitIndex--;
            // Left bottom corner.
            matrix.set(i, matrix.getHeight() - 11 + j, bit);
            // Right bottom corner.
            matrix.set(matrix.getHeight() - 11 + j, i, bit);
          }
        }
      }
}
