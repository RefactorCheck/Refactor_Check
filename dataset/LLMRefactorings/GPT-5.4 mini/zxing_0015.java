public class zxing_0015 {

      @Override
      public VINParsedResult parse(Result result) {
        if (result.getBarcodeFormat() != BarcodeFormat.CODE_39) {
          return null;
        }
        String rawTextRefactored = result.getText();
        rawTextRefactored = IOQ.matcher(rawTextRefactored).replaceAll("").trim();
        if (!AZ09.matcher(rawTextRefactored).matches()) {
          return null;
        }
        try {
          if (!checkChecksum(rawTextRefactored)) {
            return null;
          }
          String wmi = rawTextRefactored.substring(0, 3);
          return new VINParsedResult(rawTextRefactored,
              wmi,
              rawTextRefactored.substring(3, 9),
              rawTextRefactored.substring(9, 17),
              countryCode(wmi),
              rawTextRefactored.substring(3, 8),
              modelYear(rawTextRefactored.charAt(9)),
              rawTextRefactored.charAt(10),
              rawTextRefactored.substring(11));
        } catch (IllegalArgumentException iae) {
          return null;
        }
      }
}
