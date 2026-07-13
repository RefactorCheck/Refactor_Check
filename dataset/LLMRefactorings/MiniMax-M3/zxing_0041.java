public class zxing_0041 {

      Version readVersion() throws FormatException {
    
        if (parsedVersion != null) {
          return parsedVersion;
        }
    
        int dimension = bitMatrix.getHeight();
    
        int provisionalVersion = (dimension - 17) / 4;
        if (provisionalVersion <= 6) {
          return Version.getVersionForNumber(provisionalVersion);
        }
    
        Version theParsedVersion = decodeVersion(dimension, true);
        if (theParsedVersion == null) {
          theParsedVersion = decodeVersion(dimension, false);
        }
        if (theParsedVersion != null) {
          parsedVersion = theParsedVersion;
          return theParsedVersion;
        }
    
        throw FormatException.getFormatInstance();
      }

      private Version decodeVersion(int dimension, boolean isTopRight) {
        int versionBits = 0;
        int ijMin = dimension - 11;
        if (isTopRight) {
          for (int j = 5; j >= 0; j--) {
            for (int i = dimension - 9; i >= ijMin; i--) {
              versionBits = copyBit(i, j, versionBits);
            }
          }
        } else {
          for (int i = 5; i >= 0; i--) {
            for (int j = dimension - 9; j >= ijMin; j--) {
              versionBits = copyBit(i, j, versionBits);
            }
          }
        }
        Version decoded = Version.decodeVersionInformation(versionBits);
        if (decoded != null && decoded.getDimensionForVersion() == dimension) {
          return decoded;
        }
        return null;
      }
}
