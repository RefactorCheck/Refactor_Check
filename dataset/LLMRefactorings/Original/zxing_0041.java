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
    
        // Read top-right version info: 3 wide by 6 tall
        int versionBits = 0;
        int ijMin = dimension - 11;
        for (int j = 5; j >= 0; j--) {
          for (int i = dimension - 9; i >= ijMin; i--) {
            versionBits = copyBit(i, j, versionBits);
          }
        }
    
        Version theParsedVersion = Version.decodeVersionInformation(versionBits);
        if (theParsedVersion != null && theParsedVersion.getDimensionForVersion() == dimension) {
          parsedVersion = theParsedVersion;
          return theParsedVersion;
        }
    
        // Hmm, failed. Try bottom left: 6 wide by 3 tall
        versionBits = 0;
        for (int i = 5; i >= 0; i--) {
          for (int j = dimension - 9; j >= ijMin; j--) {
            versionBits = copyBit(i, j, versionBits);
          }
        }
    
        theParsedVersion = Version.decodeVersionInformation(versionBits);
        if (theParsedVersion != null && theParsedVersion.getDimensionForVersion() == dimension) {
          parsedVersion = theParsedVersion;
          return theParsedVersion;
        }
        throw FormatException.getFormatInstance();
      }
}
