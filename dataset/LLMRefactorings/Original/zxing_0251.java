public class zxing_0251 {

      ResultList encode(Version version) throws WriterException {
        if (version == null) { // compute minimal encoding trying the three version sizes.
          Version[] versions = { getVersion(VersionSize.SMALL),
                                 getVersion(VersionSize.MEDIUM),
                                 getVersion(VersionSize.LARGE) };
          ResultList[] results = { encodeSpecificVersion(versions[0]),
                                   encodeSpecificVersion(versions[1]),
                                   encodeSpecificVersion(versions[2]) };
          int smallestSize = Integer.MAX_VALUE;
          int smallestResult = -1;
          for (int i = 0; i < 3; i++) {
            int size = results[i].getSize();
            if (Encoder.willFit(size, versions[i], ecLevel) && size < smallestSize) {
              smallestSize = size;
              smallestResult = i;
            }
          }
          if (smallestResult < 0) {
            throw new WriterException("Data too big for any version");
          }
          return results[smallestResult];
        } else { // compute minimal encoding for a given version
          ResultList result = encodeSpecificVersion(version);
          if (!Encoder.willFit(result.getSize(), getVersion(getVersionSize(result.getVersion())), ecLevel)) {
            throw new WriterException("Data too big for version" + version);
          }
          return result;
        }
      }
}
